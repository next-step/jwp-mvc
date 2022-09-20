package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.apache.commons.lang3.ArrayUtils;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(String... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    public void initialize() {
        List<Object> controllers = this.controllerScanner.getControllers();
        controllers.forEach(controller -> {
            Set<Method> methods = getRequestMethods(controller);
            methods.stream().forEach(method -> addHandlerExecution(controller, method));
        });
    }

    private Set<Method> getRequestMethods(Object clazz) {
        return ReflectionUtils.getAllMethods(clazz.getClass(), ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private void addHandlerExecution(Object object, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        List<HandlerKey> handlerKeys = getHandlerKeys(requestMapping, object);
        handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(object, method)));
    }

    private List<HandlerKey> getHandlerKeys(RequestMapping requestMapping, Object object) {
        Controller controller = object.getClass().getDeclaredAnnotation(Controller.class);
        RequestMethod[] requestMethods = requestMapping.method();

        if (ArrayUtils.isEmpty(requestMethods)) {
            requestMethods = RequestMethod.values();
        }

        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(controller.value() + requestMapping.value(), requestMethod))
                .collect(Collectors.toList());
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        HandlerExecution handlerExecution = handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod().toUpperCase())));

        return handlerExecution;
    }
}
