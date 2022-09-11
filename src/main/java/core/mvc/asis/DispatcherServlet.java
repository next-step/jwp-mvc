package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.*;
import core.mvc.view.View;
import java.util.List;
import java.util.Objects;
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
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;
    private final ViewResolver viewResolver = new ViewResolver();

    @Override
    public void init() {
        initHandlerMappings();

        initHandlerAdapters();
    }

    private void initHandlerMappings() {
        RequestMapping rm = new RequestMapping();
        rm.initMapping();
        AnnotationHandlerMapping am = new AnnotationHandlerMapping();
        am.initialize();

        handlerMappings = List.of(rm, am);
    }

    private void initHandlerAdapters() {
        ControllerHandlerAdapter controllerAdapter = new ControllerHandlerAdapter();
        HandlerExecutionHandlerAdapter handlerExecutionAdapter = new HandlerExecutionHandlerAdapter();

        handlerAdapters = List.of(controllerAdapter, handlerExecutionAdapter);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        Object handler = getHandler(req);

        HandlerAdapter handlerAdapter = getHandlerAdapter(handler);

        try {
            ModelAndView modelAndView = handlerAdapter.handle(req, resp, handler);

            render(modelAndView, req, resp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Object getHandler(HttpServletRequest req) {
        return handlerMappings.stream()
                .map(it -> it.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("요청을 처리할 핸들러가 존재하지 않습니다.."));
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.supports(handler))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("핸들러 어댑터가 존재하지 않습니다."));
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (modelAndView.hasView()) {
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
            return;
        }

        View view = viewResolver.resolve(modelAndView.getViewName());
        view.render(modelAndView.getModel(), request, response);
    }
}
