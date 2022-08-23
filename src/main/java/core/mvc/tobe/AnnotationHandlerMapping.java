package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.configuration.ApplicationContext;
import core.mvc.HandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    @Override
    public void init() {
        Map<String, Object> controllers = ApplicationContext.getInstance().getBeans();
        controllers.values()
                .forEach(this::setHandlerExecutions);
    }

    @Override
    public Object getHandler(HandlerKey handlerKey) {
        return handlerExecutions.get(handlerKey);
    }

    private void setHandlerExecutions(Object controller) {
        List<Method> methods = Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(method -> this.hasRequestMapping(method.getDeclaredAnnotations()))
                .collect(Collectors.toList());

        methods.forEach(method -> this.setHandlerExecution(method, controller));
    }

    private boolean hasRequestMapping(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .anyMatch(annotation -> annotation.annotationType().equals(RequestMapping.class));
    }

    private void setHandlerExecution(Method method, Object ownerInstance) {
        Annotation requestMapping = Arrays.stream(method.getDeclaredAnnotations())
                .filter(annotation -> annotation.annotationType().equals(RequestMapping.class))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);

        String url = this.getUrl(requestMapping);
        RequestMethod requestMethod = this.getMethod(requestMapping);
        if (Objects.isNull(url) || Objects.isNull(requestMethod)) {
            throw new IllegalArgumentException();
        }

        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        HandlerExecution handlerExecution = new HandlerExecution(ownerInstance, method);
        this.handlerExecutions.put(handlerKey, handlerExecution);
    }

    private String getUrl(Annotation requestMapping) {
        Class<?> clazz = requestMapping.annotationType();
        Method pathMethod = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().equals("value"))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);

        try {
            return String.valueOf(pathMethod.invoke(requestMapping));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private RequestMethod getMethod(Annotation requestMapping) {
        Class<?> clazz = requestMapping.annotationType();
        Method pathMethod = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().equals("method"))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);

        try {
            return (RequestMethod) pathMethod.invoke(requestMapping);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

}
