package core.mvc.tobe;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.annotation.web.RequestMapping;
import core.di.factory.MethodParameter;
import core.di.factory.ParameterNameDiscoverUtils;
import core.mvc.ModelAndView;
import core.mvc.WebRequest;
import core.resolver.HandlerMethodArgumentResolver;
import core.resolver.HandlerMethodArgumentResolvers;

public class HandlerExecution {
    private final Object instance;
    private final Method method;
    private final List<MethodParameter> methodParameters;
    private final List<HandlerMethodArgumentResolver> argumentResolvers;

    private HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        this.methodParameters = ParameterNameDiscoverUtils.toMethodParameters(method);
        this.argumentResolvers = getArgumentResolvers(this.methodParameters);
    }

    public static HandlerExecution of(Object instance, Method method) {
        return new HandlerExecution(instance, method);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] args = getArgs(this.methodParameters, this.argumentResolvers, getWebRequest(request, response));
        return (ModelAndView) this.method.invoke(this.instance, args);
    }

    public Object getInstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }

    private List<HandlerMethodArgumentResolver> getArgumentResolvers(List<MethodParameter> methodParameters){
        return methodParameters.stream()
                .map(HandlerMethodArgumentResolvers.getInstance()::getResolver)
                .collect(toList());
    }

    private WebRequest getWebRequest(HttpServletRequest request, HttpServletResponse response) {
        return WebRequest.of(method.getAnnotation(RequestMapping.class), request, response);
    }

    private Object[] getArgs(List<MethodParameter> methodParameters
            , List<HandlerMethodArgumentResolver> argumentResolvers
            , WebRequest webRequest ) {

        Object[] args = new Object[methodParameters.size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = argumentResolvers.get(i).resolve(methodParameters.get(i), webRequest);
        }
        return args;
    }
}
