package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.tobe.resolver.argument.ArgumentResolvers;
import core.mvc.tobe.resolver.argument.HandlerMethodArgumentResolver;
import core.mvc.tobe.resolver.argument.MethodParameter;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HandlerExecution implements HandlerCommand {

    private Object object;
    private final Method method;
    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public HandlerExecution(final Object obj, final Method method) {
        this.object = obj;
        this.method = method;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Object[] parameters = getMethodExecuteParameter(request, response, method);

            return (ModelAndView) method.invoke(object, parameters);
        } catch (Throwable e) {
            throw new IOException(e);
        }

    }

    private Object[] getMethodExecuteParameter(HttpServletRequest request, HttpServletResponse response, Method method) {

        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            Parameter parameter = method.getParameters()[i];
            String parameterName = parameterNames[i];

            MethodParameter methodParameter = new MethodParameter(method, parameterName, parameter);

            HandlerMethodArgumentResolver resolver = ArgumentResolvers.getResolver(methodParameter);

            Object value = resolver.resolve(request, response, methodParameter);

            values[i] = value;
        }

        return values;
    }

}
