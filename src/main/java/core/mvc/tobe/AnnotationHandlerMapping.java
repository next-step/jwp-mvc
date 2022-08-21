package core.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Set<Class<?>> targetClasses) {
        for (Class<?> targetClass : targetClasses) {
            var targetObject = createTarget(targetClass);

            var methods = Arrays.stream(targetClass.getMethods())
                .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());

            var handlers = methods.stream()
                .map(it -> Map.entry(createKey(it), new HandlerExecution(targetObject, it)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            handlerExecutions.putAll(handlers);
        }
    }

    private HandlerKey createKey(Method method) {
        var declaredAnnotation = method.getDeclaredAnnotation(RequestMapping.class);

        return new HandlerKey(declaredAnnotation.value(), declaredAnnotation.method());
    }

    private Object createTarget(Class<?> targetClass) {
        try {
            return targetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            LOGGER.info("AnnotationHandlerMapping.createTarget 해당 클래스를 생성할 수 없습니다. {}", e.getMessage());
            throw new IllegalArgumentException("해당 클래스를 생성할 수 없습니다. " + targetClass.getName(), e);
        }
    }

    @Override
    public Handler getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return new AnnotationHandler(handlerExecutions.get(new HandlerKey(requestUri, rm)));
    }

    private static class AnnotationHandler implements Handler {

        private final HandlerExecution handlerExecution;

        private AnnotationHandler(HandlerExecution handlerExecution) {
            this.handlerExecution = handlerExecution;
        }

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
            var modelAndView = handlerExecution.handle(request, response);

            if (modelAndView != null) {
                modelAndView.render(request, response);
            }
        }
    }
}
