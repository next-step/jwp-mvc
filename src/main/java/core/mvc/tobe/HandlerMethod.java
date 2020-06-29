package core.mvc.tobe;

import core.mvc.ModelAndView;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class HandlerMethod {

    private Method method;
    private HandlerExecution handlerExecution;

    public HandlerMethod(Method method) {
        this.method = method;
        this.handlerExecution = args -> (ModelAndView) method.invoke(method.getDeclaringClass().newInstance(), args);
    }

    public HandlerMethod(Method method, Object instance) {
        this.method = method;
        this.handlerExecution = args -> (ModelAndView) method.invoke(instance, args);
    }

    public ModelAndView handle(Object... args) throws Exception {
        return handlerExecution.handle(args);
    }
}
