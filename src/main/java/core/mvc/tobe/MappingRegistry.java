package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 매핑 정보를 관리하는 클래스
 */
public class MappingRegistry {

    private static final int FIRST_METHOD = 0;

    private final Map<HandlerKey, HandlerExecution> registry = new HashMap<>();

    public void register(Map<Class<?>, Object> classMap) {
        for (Class<?> clazz : classMap.keySet()) {
            Object instance = classMap.get(clazz);

            initRegistry(clazz, instance);
        }
    }

    private void initRegistry(Class<?> clazz, Object instance) {
        String basePath = clazz.getAnnotation(Controller.class).path();

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                String requestUri = basePath + requestMapping.value();
                RequestMethod requestMethod = RequestMethod.from(requestMapping.method()[FIRST_METHOD]);

                HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);

                registry.put(handlerKey, new HandlerExecution(instance, method));
            }
        }
    }

    public HandlerExecution getHandlerExecution(HandlerKey handlerKey) {
        return registry.get(handlerKey);
    }
}
