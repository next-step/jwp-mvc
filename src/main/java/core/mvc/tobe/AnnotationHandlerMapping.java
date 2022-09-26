package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.exception.HandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        controllerScanner.controllers()
                .forEach((key, value) -> Arrays.stream(key.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> addHandlerExecution(value, method)));
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) throws ServletException {
        RequestMethod requestMethod = RequestMethod.requestMethod(request.getMethod());
        String requestURI = request.getRequestURI();

        return handlerExecutions.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isMatched(requestMethod, requestURI))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new HandlerException("요청을 처리할 수 없습니다."));
    }

    private void addHandlerExecution(Object controller, Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        if (mapping.method().equals(RequestMethod.ALL)) {
            addAllMethodHandlerExecutions(controller, method, mapping);
            return;
        }

        handlerExecutions.put(new HandlerKey(mapping.value(), mapping.method()), new HandlerExecution(controller, method));
    }

    private void addAllMethodHandlerExecutions(Object controller, Method method, RequestMapping mapping) {
        Arrays.stream(RequestMethod.values())
                .forEach(value -> handlerExecutions.put(
                        new HandlerKey(mapping.value(), value),
                        new HandlerExecution(controller, method))
                );
    }
}

