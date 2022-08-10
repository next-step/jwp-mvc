package core.mvc.asis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.HandlerAdapter;
import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecutionHandlerAdapter;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "next.controller";

    private List<HandlerMapping> mappings = new ArrayList<>();
    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();


    @Override
    public void init() {
        LegacyHandlerMapping legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        annotationHandlerMapping.initialize();

        mappings.add(legacyHandlerMapping);
        mappings.add(annotationHandlerMapping);

        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new HandlerExecutionHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object mappingHandler = getHandler(req);
        HandlerAdapter handlerAdapter = getHandlerAdapter(mappingHandler);

        try {
            ModelAndView mv = handlerAdapter.handle(req, resp, mappingHandler);
            render(req, resp, mv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getHandler (HttpServletRequest request) {
        return mappings.stream()
                       .map(mapping -> mapping.getHandler(request))
                       .findFirst()
                       .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 handler 입니다."));
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                              .filter(handlerAdapter -> handlerAdapter.supports(handler))
                              .findFirst()
                              .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 Adapter 입니다."));
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws Exception {
        modelAndView.getView().render(modelAndView.getModel(), req, resp);
    }
}
