package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.checkerframework.checker.units.qual.A;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        Object[] arguments = Stream.of(new HttpRequestArgumentResolver(request, method),
                        new HttpResponseArgumentResolver(response, method),
                        new PathVariableArgumentResolver(request, method),
                        new ParameterArgumentResolver(request, method))
                .flatMap(argumentResolver -> Arrays.stream(argumentResolver.resolve())
                        .collect(Collectors.toList())
                        .stream())
                .toArray();
        return (ModelAndView) method.invoke(controller, arguments);
    }
}
