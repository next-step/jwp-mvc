package core.mvc;

import core.mvc.handler.HandlerExecution;
import core.mvc.handlerMapping.AnnotationHandlerMapping;
import core.mvc.handlerMapping.HandlerMappings;
import core.mvc.support.exception.HandlerNotFoundException;
import core.mvc.view.JspView;
import core.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "next.controller";

    private HandlerMappings handlerMappings;
    private HandlerAdapter handlerAdapter;

    @Override
    public void init() {
        final AnnotationHandlerMapping ahm = new AnnotationHandlerMapping(BASE_PACKAGE);
        ahm.initialize();

        handlerMappings = new HandlerMappings();
        handlerMappings.add(ahm);

        handlerAdapter = new HandlerAdapter();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        try {
            ModelAndView mav = dispatch(req, resp);
            render(mav, req, resp);
        } catch (Exception e) {
            logger.error("Exception is: ", e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView dispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            final Object handler = getHandler(req);
            return handle(handler, req, resp);
        } catch (HandlerNotFoundException handlerNotFoundException) {
            return new ModelAndView(new JspView("/error/page404.jsp"));
        }
    }

    private ModelAndView handle(Object handler, HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        if (handler instanceof HandlerExecution) {
            return handlerAdapter.handle((HandlerExecution) handler, req, resp);
        }
        throw new ServletException("handler not Found");
    }

    private Object getHandler(HttpServletRequest req) {
        return handlerMappings.getHandler(req);
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        final Map<String, Object> model = mav.getModel();
        final View view = mav.getView();

        view.render(model, req, resp);
    }

}
