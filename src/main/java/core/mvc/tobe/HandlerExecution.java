package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.tobe.resolver.HttpRequestArgumentResolver;
import core.mvc.tobe.resolver.HttpResponseArgumentResolver;
import core.mvc.tobe.resolver.ParameterArgumentResolver;
import core.mvc.tobe.resolver.PathVariableArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HandlerExecution {

    final private Object controller;
    final private Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] arguments = Stream.of(HttpRequestArgumentResolver.getInstance(),
                        HttpResponseArgumentResolver.getInstance(),
                        PathVariableArgumentResolver.getInstance(),
                        ParameterArgumentResolver.getInstance())
                .flatMap(argumentResolver -> Arrays.stream(argumentResolver.resolve(request, response, method))
                        .collect(Collectors.toList())
                        .stream())
                .toArray();
        return (ModelAndView) method.invoke(controller, arguments);
    }
}
