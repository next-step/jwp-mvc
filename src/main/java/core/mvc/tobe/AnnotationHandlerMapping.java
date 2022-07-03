package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

            initHandlerExecutions(controllers);
        }
    }

    private void initHandlerExecutions(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            List.of(controller.getDeclaredMethods()).stream()
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                    HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
                    handlerExecutions.put(handlerKey, (request, response) -> (ModelAndView) method.invoke(controller.getDeclaredConstructor()
                                                                                                                    .newInstance(), request, response));
                });
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod()
                                                        .toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
