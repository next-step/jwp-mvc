package core.mvc.resolver;

import core.annotation.web.PathVariable;
import core.mvc.tobe.MethodParameter;
import org.springframework.web.util.pattern.PathPatternParser;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static core.mvc.resolver.PathPatternUtil.getUriVariable;

public class PathVariableArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasAnnotationInParameter(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        String object = getObject(parameter, request);

        return ResolverUtil.convertPrimitiveType(parameter.getParameterType(), object);
    }

    private String getObject(MethodParameter parameter, HttpServletRequest request) {
        String pattern = parameter.getRequestMappingValue();
        String requestURI = request.getRequestURI();
        String key = getKey(parameter);

        return getUriVariable(pattern, requestURI, key);
    }

    private String getKey(MethodParameter parameter) {
        String pathValue = parameter.getPathVariableValue();

        return pathValue.isEmpty() ? parameter.getParameterName() : pathValue;
    }
}
