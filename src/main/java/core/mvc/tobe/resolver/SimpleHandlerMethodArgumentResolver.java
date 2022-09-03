package core.mvc.tobe.resolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import core.annotation.web.PathVariable;
import core.mvc.tobe.utils.TypeUtils;

public class SimpleHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final Set<Class<?>> supportClass = Set.of(
        Long.class, String.class, Integer.class, int.class, long.class
    );

    @Override
    public boolean supportsParameter(Parameter parameter) {
        return !parameter.isAnnotationPresent(PathVariable.class)
            && supportClass.contains(parameter.getType());
    }

    @Override
    public Object resolve(String parameterName, Parameter parameter, HttpServletRequest request, Method method) {
        var value = request.getParameter(parameterName);

        Class<?> targetType = parameter.getType();

        return TypeUtils.stringToType(targetType, value);
    }
}
