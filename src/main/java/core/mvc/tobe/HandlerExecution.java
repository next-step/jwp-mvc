package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.tobe.resolver.ArgumentResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class HandlerExecution {

    private final Method method;
    private List<ArgumentResolver> argumentResolverList;

    public HandlerExecution(Method method, List<ArgumentResolver> argumentResolverList) {
        this.method = method;
        this.argumentResolverList = argumentResolverList;
    }

    public Object handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Class<?> declaringClass = method.getDeclaringClass();
        Constructor<?> constructor = declaringClass.getConstructor();
        Object newInstance = constructor.newInstance();

        String[] parameterNames = getParameterNames();
        Parameter[] methodParameters = method.getParameters();
        Object[] parameters = new Object[methodParameters.length];

        for (int i = 0; i < methodParameters.length; i++) {
            Parameter parameter = methodParameters[i];
            MethodParameter methodParameter = new MethodParameter(method, parameter.getType(), parameter.getAnnotations(), parameterNames[i]);
            parameters[i] = getParameter(methodParameter, request, response);
        }

        return method.invoke(newInstance, parameters);
    }

    private String[] getParameterNames() {
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        return parameterNames;
    }

    private Object getParameter(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        for (ArgumentResolver resolver : argumentResolverList) {
            if (resolver.supportsParameter(methodParameter)) {
                return resolver.resolveArgument(methodParameter, request, response);
            }
        }

        throw new IllegalArgumentException("적절한 resolver를 찾을 수 없습니다.");
    }
}
