package core.mvc.tobe;

import core.mvc.tobe.support.ArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class HandlerExecution {

    private List<ArgumentResolver> argumentResolver;
    private Object target;
    private Method method;


    public HandlerExecution(List<ArgumentResolver> argumentResolvers, Object target, Method method) {
        this.argumentResolver = argumentResolvers;
        this.target = target;
        this.method = method;
    }

    public Object handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] parameters = new Object[method.getParameterCount()];
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameters.length; i++) {
            MethodParameter methodParameter = new MethodParameter(parameterTypes[i], parameterAnnotations[i]);
            parameters[i] = getParameter(methodParameter, request, response);
        }

        return method.invoke(target, parameters);
    }

    private Object getParameter(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {

        for (ArgumentResolver resolver : argumentResolver) {
            if (resolver.supports(methodParameter)) {
                return resolver.resolveArgument(methodParameter, request, response);
            }
        }

        return new Object();
    }


}
