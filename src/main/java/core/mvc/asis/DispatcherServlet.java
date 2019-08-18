package core.mvc.asis;

import com.google.common.collect.Lists;
import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.ExceptionConsumerSupplier;
import core.mvc.tobe.ExceptionFunctionSupplier;
import core.mvc.tobe.HandlerMapping;
import core.mvc.tobe.handleradapter.ControllerHandlerAdapter;
import core.mvc.tobe.handleradapter.HandlerAdapter;
import core.mvc.tobe.handleradapter.HandlerExecutionHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final String ANNOTATION_CONTROLLER_PACKAGE = "next.controller";

    private List<HandlerMapping> handlerMappings = Lists.newArrayList();
    private List<HandlerAdapter> handlerAdapters = Lists.newArrayList();

    @Override
    public void init() {
        handlerMappingsInit();
        handlerAdaptersInit();
    }

    private void handlerAdaptersInit() {
        handlerAdapters.add(new HandlerExecutionHandlerAdapter());
        handlerAdapters.add(new ControllerHandlerAdapter());
    }

    private void handlerMappingsInit() {
        LegacyHandlerMapping legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(ANNOTATION_CONTROLLER_PACKAGE);
        annotationHandlerMapping.initialize();

        handlerMappings.add(legacyHandlerMapping);
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        Optional<Object> handler = getHandler(request);
        try {
            move(handler, request, response);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Optional<Object> getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny();
    }

    private void move(Optional<Object> handlerOptional, HttpServletRequest request, HttpServletResponse response) {
        if (!handlerOptional.isPresent()) {
            moveNotFound(response);
            return;
        }

        Object handler = handlerOptional.get();
        handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .map(ExceptionFunctionSupplier.wrap(handlerAdapter -> handlerAdapter.handle(request, response, handler)))
                .findAny()
                .ifPresent(ExceptionConsumerSupplier.wrap(modelAndView -> render(request, response, modelAndView)));
    }

    private void moveNotFound(HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
    }

    private void render(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
        String viewName = modelAndView.getViewName();
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        modelAndView.render(request, response);
    }
}
