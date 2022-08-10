package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerAdapter;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String[] BASE_PACKAGES = new String[] {"next.controller"};

    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() {
        annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGES);
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        HandlerAdapter handlerAdapter = annotationHandlerMapping.getHandler(req);
        if (Objects.nonNull(handlerAdapter)) {
            try {
                ModelAndView modelAndView = handlerAdapter.handle(req, resp);
                move(modelAndView, req, resp);
            } catch (Exception e) {
                logger.error("Exception : {}", e);
                e.printStackTrace();
            }
            return;
        }

        throw new ServletException("Invalid request");
    }

    private void move(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        modelAndView.getView().render(modelAndView.getModel(), req, resp);
    }

}
