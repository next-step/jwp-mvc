package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.view.HandlerExecutionViewResolver;
import core.mvc.view.View;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javassist.NotFoundException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ANNOTATION_HANDLER_BASE_PACKAGE = "next.controller";
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private AnnotationHandlerMapping handlerMapping;
    private HandlerExecutionViewResolver viewResolver;

    @Override
    public void init() {
        handlerMapping = new AnnotationHandlerMapping(ANNOTATION_HANDLER_BASE_PACKAGE);
        handlerMapping.initialize();
        viewResolver = new HandlerExecutionViewResolver();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        try {
            Object handler = handlerMapping.getHandler(req);
            ModelAndView mav = viewResolver.resolve(handler, req, resp);
            View view = mav.getView();
            view.render(mav.getModel(), req, resp);
        } catch (NotFoundException ne) {
            logger.error("Not found : {}", ne.getMessage());
            resp.sendError(HttpStatus.NOT_FOUND.value(), ne.getMessage());
        } catch (InvocationTargetException ie) {
            logger.error("Invocation Exception : {}", ie.getTargetException().getMessage());
            resp.sendError(HttpStatus.FORBIDDEN.value(), ie.getTargetException().getMessage());
        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }
}
