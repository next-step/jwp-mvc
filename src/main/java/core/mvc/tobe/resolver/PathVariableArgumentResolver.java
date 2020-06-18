package core.mvc.tobe.resolver;

import core.annotation.web.PathVariable;
import next.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PathVariableArgumentResolver extends AbstractHandlerMethodArgumentResolver {
    public static final Class<PathVariable> PATH_VARIABLE_ANNOTATION_CLASS = PathVariable.class;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.isAnnotationPresent(PATH_VARIABLE_ANNOTATION_CLASS);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return resolvePathVariableArgument(parameter, parameter.getName(), parameter.getType());
    }

    private Object resolvePathVariableArgument(MethodParameter parameter, String name, Class<?> type) {
        PathVariable pathVariable = parameter.getAnnotation(PATH_VARIABLE_ANNOTATION_CLASS);
        name = StringUtils.getOrDefault(StringUtils.getOrDefault(pathVariable.name(), pathVariable.value()), name);
        return resolveArgument(parameter.getPathVariableValue(name), type);
    }
}
