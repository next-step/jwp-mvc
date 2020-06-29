package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HandlerExecution {

    private Object instance;
    private Method method;
    private static ArgumentResolvers argumentResolvers;
    private static ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    static {
        argumentResolvers = new ArgumentResolvers();
        argumentResolvers.add(new PathVariableArgumentResolver());
        argumentResolvers.add(new RequestParameterArgumentResolver());
        argumentResolvers.add(new HttpRequestArgumentResolver());
        argumentResolvers.add(new HttpResponseArgumentResolver());
        argumentResolvers.add(new JavaBeanArgumentResolver());
    }

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] resolveObject = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, parameterNames[i], parameters[i].getType(), parameters[i].getAnnotations());
            HandlerMethodArgumentResolver argumentResolver = argumentResolvers.getResolver(methodParameter);
            resolveObject[i] = argumentResolver.resolve(methodParameter, request, response);
        }
        Object modelAndView = method.invoke(instance, resolveObject);
        return (ModelAndView) modelAndView;
    }
}
