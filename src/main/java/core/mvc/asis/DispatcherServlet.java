package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.*;
import core.mvc.tobe.handlerAdapter.HandlerAdapter;
import core.mvc.tobe.handlerAdapter.HandlerAdapterStorage;

import core.mvc.view.View;
import exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerAdapterStorage handlerAdapterStorage;
    private RequestMapping requestMapping;
    private AnnotationHandlerMapping AnnotationHandlerMapping;
    private final List<HandlerMapping> mappings = new ArrayList<>();

    @Override
    public void init() {
        handlerAdapterStorage = new HandlerAdapterStorage();
        handlerAdapterStorage.init();

        requestMapping = new RequestMapping();
        requestMapping.initMapping();

        AnnotationHandlerMapping = new AnnotationHandlerMapping();
        AnnotationHandlerMapping.initMapping();

        mappings.add(requestMapping);
        mappings.add(AnnotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Object handler = getHandler(request);
        if (handler == null) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        HandlerAdapter adapter = handlerAdapterStorage.getHandlerAdapter(handler);
        handleAdapter(request, response, handler, adapter);
    }

    private void handleAdapter(HttpServletRequest request, HttpServletResponse response,
                               Object handler, HandlerAdapter adapter) throws ServletException {
        try {
            handle(request, response, handler, adapter);
        } catch (Exception e) {
            logger.error("Exception: {}", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    private void handle(HttpServletRequest request, HttpServletResponse response,
                        Object handler, HandlerAdapter adapter) throws Exception {
        ModelAndView modelAndView = adapter.handle(request, response, handler);
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }

    private Object getHandler(HttpServletRequest request) {
        return mappings.stream()
                .filter(handlerMapping -> handlerMapping.isSupported(request))
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 URL 주소입니다."));
    }
}
