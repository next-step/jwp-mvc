package core.mvc.tobe;

import core.mvc.Handler;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

import core.mvc.exepction.HandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerAdapter implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerAdapter.class);

    private final AnnotationHandlerMapping annotationHandlerMapping;

    public AnnotationHandlerAdapter(AnnotationHandlerMapping annotationHandlerMapping) {
        this.annotationHandlerMapping = annotationHandlerMapping;
        annotationHandlerMapping.initialize();
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        HandlerExecution handler = getHandler(request);

        if (handler == null) {
            return null;
        }

        try {
            return handler.handle(request, response);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new HandlerException("Failed to invoke method", e);
        }
    }

    @Override
    public boolean isSupport(HttpServletRequest req) {
        return getHandler(req) != null;
    }

    private HandlerExecution getHandler(HttpServletRequest request) {
        return annotationHandlerMapping.getHandler(request);
    }
}
