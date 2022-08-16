package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.ViewResolver;
import core.mvc.tobe.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private RequestMapping rm;

    private List<HandlerMapping> handlerMappings;

    private List<HandlerAdapter> handlerAdapters;

    private final ViewResolver viewResolver = new ViewResolver();

    @Override
    public void init() throws ServletException {
        initRequestMapping();

        initHandlerMappings();

        initHandlerAdapters();
    }

    private void initRequestMapping() {
        rm = new RequestMapping();
        rm.initMapping();
    }

    private void initHandlerMappings() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("next");
        annotationHandlerMapping.initialize();

        this.handlerMappings = List.of(
                new RequestMappingHandlerMapping(rm),
                annotationHandlerMapping
        );
    }

    private void initHandlerAdapters() {
        this.handlerAdapters = List.of(
                new ControllerHandlerAdapter(),
                new HandlerExecutionHandlerAdapter()
        );
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Object handler = getHandler(request);

            HandlerAdapter handlerAdapter = getHandlerAdapter(handler);

            ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);

            render(modelAndView, request, response);
        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.isSupport(handler))
                .findAny()
                .orElseThrow();
    }

    private Object getHandler(HttpServletRequest request) {
        return this.handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new IllegalStateException(String.format("요청을 처리할 핸들러가 없습니다. [%s %s]", request.getMethod(), request.getRequestURI())));
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (modelAndView.hasView()) {
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
            return;
        }

        View view = viewResolver.resolveViewName(modelAndView.getViewName());
        view.render(modelAndView.getModel(), request, response);
    }
}
