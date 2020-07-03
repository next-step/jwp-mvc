package core.mvc.tobe;

import core.mvc.ModelAndView;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class HandlerMethod {

    private Method method;
    private HandlerExecution handlerExecution;
    private String requestMappingUri;

    public HandlerMethod(Method method, String requestMappingUri) {
        this.method = method;
        this.handlerExecution = args -> (ModelAndView) method.invoke(method.getDeclaringClass().newInstance(), args);
        this.requestMappingUri = requestMappingUri;
    }

    public HandlerMethod(Method method, String requestMappingUri, Object instance) {
        this.method = method;
        this.handlerExecution = args -> (ModelAndView) method.invoke(instance, args);
        this.requestMappingUri = requestMappingUri;
    }

    public ModelAndView handle(Object... args) throws Exception {
        return handlerExecution.handle(args);
    }
}
