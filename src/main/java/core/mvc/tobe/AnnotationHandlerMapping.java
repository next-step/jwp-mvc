package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
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
            Reflections reflections = new Reflections(((String) basePackage));
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

            initHandlerExcutions(controllers);
        }
    }

    private void initHandlerExcutions(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            List.of(controller.getDeclaredMethods()).stream()
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    List<HandlerKey> handlerKeys = initHandlerKeys(requestMapping);

                    handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, (request, response) -> (ModelAndView) method.invoke(controller.getDeclaredConstructor()
                                                                                                                                                      .newInstance(), request, response)));
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
