package core.mvc.tobe;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.ModelAndView;
import core.mvc.tobe.resolver.HandlerMethodArgumentResolver;
import core.mvc.tobe.resolver.JavabeansHandlerMethodArgumentResolver;
import core.mvc.tobe.resolver.PathVariableHandlerMethodArgumentResolver;
import core.mvc.tobe.resolver.SimpleHandlerMethodArgumentResolver;
import core.mvc.tobe.utils.ParameterSupport;

public class HandlerExecution {

    private static final List<HandlerMethodArgumentResolver> argumentResolvers = List.of(
        new SimpleHandlerMethodArgumentResolver(),
        new JavabeansHandlerMethodArgumentResolver(),
        new PathVariableHandlerMethodArgumentResolver()
    );

    private final Object targetObject;
    private final Method method;

    public HandlerExecution(Object targetObject, Method method) {
        this.targetObject = targetObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        var parameterNames = ParameterSupport.nameDiscoverer()
            .getParameterNames(method);
        var parameters = method.getParameters();
        var values = new Object[parameterNames.length];

        for (int i = 0; i < parameterNames.length; i++) {
            var parameterName = parameterNames[i];
            var parameter = parameters[i];

            var resolver = argumentResolvers.stream()
                .filter(it -> it.supportsParameter(parameter))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않은 파라미터 타입입니다" + parameter.getName()));

            var value = resolver.resolve(parameterName, parameter, request, method);
            values[i] = value;
        }

        return (ModelAndView)method.invoke(targetObject, values);
    }
}

