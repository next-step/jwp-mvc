package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerAdapter;
import core.mvc.tobe.HandlerExecutionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private LegacyRequestMapping rm;
    private AnnotationHandlerMapping annotationHandlerMapping;
    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    @Override
    public void init() throws ServletException {
        rm = new LegacyRequestMapping();
        rm.initMapping();
        annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        annotationHandlerMapping.initialize();
        handlerMappings = List.of(rm, annotationHandlerMapping);
        handlerAdapters = List.of(new ControllerHandlerAdapter(), new HandlerExecutionAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        try {
            Object handler = getHandler(req);
            if (handler == null) {
                throw new ServletException(String.format("%s의 handler 를 찾을 수 없습니다.", req.getRequestURI()));
            }
            handle(handler, req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest req) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(req);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    private void handle(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            execute(handler, req, resp, handlerAdapter);
        }
    }

    private void execute(Object handler, HttpServletRequest req, HttpServletResponse resp, HandlerAdapter handlerAdapter) throws Exception {
        if (handlerAdapter.supports(handler)) {
            ModelAndView modelAndView = handlerAdapter.handle(req, resp, handler);
            render(modelAndView, req, resp);
        }
    }

    private void render(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), req, resp);
    }

}
