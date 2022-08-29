package core.mvc;

import core.mvc.adapter.ControllerHandlerAdapter;
import core.mvc.adapter.HandlerAdapter;
import core.mvc.adapter.HandlerExecutionHandlerAdapter;
import core.mvc.asis.LegacyRequestMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.view.ModelAndView;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "next.controller";

    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        annotationHandlerMapping.initialize();
        LegacyRequestMapping legacyRequestMapping = new LegacyRequestMapping();
        legacyRequestMapping.initMapping();
        handlerMappings.add(annotationHandlerMapping);
        handlerMappings.add(legacyRequestMapping);
        handlerAdapters.add(new HandlerExecutionHandlerAdapter());
        handlerAdapters.add(new ControllerHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        try {
            handle(request, response);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = getHandler(request);
        HandlerAdapter handlerAdapter = handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new NotFoundException("핸들러 처리를 지원하는 어댑터가 존재하지 않습니다."));
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
        modelAndView.render(request, response);
    }

    private Object getHandler(HttpServletRequest request) throws NotFoundException {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .findAny()
                .orElseThrow(() -> new NotFoundException("요청을 처리할 핸들러가 존재하지 않습니다."));
    }
}
