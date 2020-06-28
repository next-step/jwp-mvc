package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private Object instance;
    private Method method;
    private ArgumentResolvers argumentResolvers;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        initialize();
    }

    private void initialize() {
        argumentResolvers = new ArgumentResolvers();
        argumentResolvers.add(new PathVariableArgumentResolver());
        argumentResolvers.add(new RequestParameterArgumentResolver());
        argumentResolvers.add(new JavaBeanArgumentResolver());
        argumentResolvers.add(new HttpRequestArgumentResolver());
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethodArgumentResolver argumentResolver = argumentResolvers.getResolver(method);
        Object[] resolveObject = argumentResolver.resolve(method, request, response);
        Object modelAndView = method.invoke(instance, resolveObject);
        return (ModelAndView) modelAndView;
    }
}
