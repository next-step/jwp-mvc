package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import org.reflections.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {
    private Object[] basePackages;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackages) {
        this.basePackages = basePackages;
    }

    public void initialize() {
        for (Object basePackage : basePackages) {
            ControllerScanner controllerScanner = new ControllerScanner((String) basePackage);
            Map<Class, Object> controllers = controllerScanner.getControllers();

            initHandlerExcutions(controllers);
        }
    }

    private void initHandlerExcutions(Map<Class, Object> controllers ) {
        for (Class<?> controller : controllers.keySet()) {
            Set<Method> methods = ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));
            methods.forEach(method -> {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                List<HandlerKey> handlerKeys = initHandlerKeys(requestMapping);

                handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey,
                        (request, response) -> (ModelAndView) method.invoke(controllers.get(controller), request, response)));
            });
        }
    }

    private List<HandlerKey> initHandlerKeys(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            return HandlerKey.allMethodsKey(requestMapping.value());
        }

        return Arrays.stream(requestMethods)
                     .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                     .collect(Collectors.toList());
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod()
                                                        .toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
