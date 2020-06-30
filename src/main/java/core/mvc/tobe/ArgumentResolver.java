package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

public interface ArgumentResolver {

    public void applyExecution(Method method, HandlerExecution handlerExecution) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        if (requestMapping.method().equals(RequestMethod.ALL)) {
            applyExecutionByAllMethod(requestMapping, handlerExecution);
            return;
        }
        final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
        AnnotationHandlerMapping.handlerExecutions.put(handlerKey, handlerExecution);
    }

    private void applyExecutionByAllMethod(RequestMapping requestMapping, HandlerExecution handlerExecution) {
        Arrays.stream(RequestMethod.values())
                .forEach(requestMethod -> {
                    if (!requestMethod.equals(RequestMethod.ALL)) {
                        AnnotationHandlerMapping.handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMethod), handlerExecution);
                    }
                });
    }
}
