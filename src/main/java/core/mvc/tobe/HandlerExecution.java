package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HandlerExecution {

    private final Object handler;
    private final Method method;
    private final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final ArgumentResolvers argumentResolvers = new ArgumentResolvers();

    public HandlerExecution(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MethodParameter[] methodParameters = getMethodParameters();
        Object[] arguments = new Object[methodParameters.length];
        for (int i = 0; i < methodParameters.length; i++) {
            arguments[i] = resolveArgument(methodParameters[i], request, response);
        }
        return (ModelAndView) method.invoke(handler, arguments);
    }

    private MethodParameter[] getMethodParameters() {
        Class<?>[] parameterTypes = method.getParameterTypes();
        MethodParameter[] methodParameters = new MethodParameter[parameterTypes.length];
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();

        for (int index = 0; index < parameterTypes.length; index++) {
            Annotation[] annotations = parameters[index].getAnnotations();
            methodParameters[index] = new MethodParameter(parameterTypes[index], method, parameterNames[index], annotations);
        }

        return methodParameters;
    }

    private Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return argumentResolvers.resolve(parameter, request, response);
    }
}
