package core.mvc.tobe.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.ReflectionUtils;
import next.util.StringUtils;
import org.springframework.web.util.pattern.PathPattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static core.mvc.tobe.ReflectionUtils.REQUEST_MAPPING_ANNOTATION_CLASS;

public class PathVariableArgumentResolver extends AbstractHandlerMethodArgumentResolver {
    public static final Class<PathVariable> PATH_VARIABLE_ANNOTATION_CLASS = PathVariable.class;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.isAnnotationPresent(PATH_VARIABLE_ANNOTATION_CLASS);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return resolve(request.getParameterMap(), parameter.getParameter(), parameter.getName(), parameter.getType());
    }

    public Object resolve(Map<String, String> pathVariables, Parameter parameter, String name, Class<?> type) {
        PathVariable pathVariable = parameter.getAnnotation(PATH_VARIABLE_ANNOTATION_CLASS);
        name = StringUtils.getOrDefault(StringUtils.getOrDefault(pathVariable.name(), pathVariable.value()), name);
        return resolve(pathVariables, name, type);
    }

}
