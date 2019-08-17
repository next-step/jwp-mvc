package core.mvc.asis;

import core.mvc.HandlerAdapter;
import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.ServletHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_CONTROLLER_PACKAGE = "next.controller";

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
    }

    private void initHandlerMappings() {
        handlerMappings = new ArrayList<>();
        handlerMappings.add(new RequestMapping());
        handlerMappings.add(new AnnotationHandlerMapping(BASE_CONTROLLER_PACKAGE));
    }

    private void initHandlerAdapters() {
        handlerAdapters = new ArrayList<>();
        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new ServletHandlerAdapter());
    }

    private HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new ServletException("No adapter for handler [" + handler + "]"));
    }

    private Object getHandler(HttpServletRequest request) throws ServletException {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new ServletException("No handler mapping"));
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            Object handler = getHandler(req);
            HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            ModelAndView mav = handlerAdapter.handle(req, resp, handler);
            View view = mav.getView();
            view.render(mav.getModel(), req, resp);
        } catch (Exception e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
}
