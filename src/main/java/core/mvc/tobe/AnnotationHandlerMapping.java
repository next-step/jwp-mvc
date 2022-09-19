package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(String... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    @Override
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
        Controller controller = object.getClass().getDeclaredAnnotation(Controller.class);
        handlerExecutions.put(new HandlerKey(controller.value() + requestMapping.value(), requestMapping.method()), new HandlerExecution(object, method));
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        HandlerExecution handlerExecution = handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod().toUpperCase())));

        if (Objects.isNull(handlerExecution)) {
            return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.NONE));
        }
        return handlerExecution;
    }
}
