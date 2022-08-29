package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.resolver.ArgumentResolver;
import core.mvc.resolver.RequestParameter;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class HandlerExecution {

    private List<ArgumentResolver> argumentResolver;
    private Object declaredObject;
    private Method method;

    public HandlerExecution(List<ArgumentResolver> argumentResolver, Object declaredObject, Method method) {
        this.argumentResolver = argumentResolver;
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] parameterNames = getParameterNames();
        Parameter[] methodParameters = method.getParameters();
        Object[] parameters = new Object[methodParameters.length];
        for (int i = 0; i < methodParameters.length; i++) {
            Parameter methodParameter = methodParameters[i];

            RequestParameter requestParameter = new RequestParameter(method, methodParameter, parameterNames[i]);

            parameters[i] = getParameter(requestParameter, request, response);
        }

        return (ModelAndView) method.invoke(declaredObject, parameters);
    }

    private String[] getParameterNames() {
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        return parameterNames;
    }

    private Object getParameter(RequestParameter requestParameter, HttpServletRequest request, HttpServletResponse response) {
        for (ArgumentResolver resolver : argumentResolver) {
            if (resolver.supports(requestParameter)) {
                return resolver.resolveArgument(requestParameter, request, response);
            }
        }

        throw new IllegalArgumentException("Not Found Adaptable Resolver");
    }
}
