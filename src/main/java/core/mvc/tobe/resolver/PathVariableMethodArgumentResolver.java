package core.mvc.tobe.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import next.support.PathPatternUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class PathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(Parameter parameter) {
        if (parameter.isAnnotationPresent(PathVariable.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(Parameter parameter, String parameterName, Method method, HttpServletRequest request, HttpServletResponse response) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String path = requestMapping.value();
        Map<String, String> uriVariables = PathPatternUtils.parse(path)
                                                           .matchAndExtract(PathPatternUtils.toPathContainer(request.getRequestURI()))
                                                           .getUriVariables();
        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);

        return ParameterTypeConverter.convert(parameter.getType(), getVariable(uriVariables, pathVariable, parameterName));
    }

    private String getVariable(Map<String, String> uriVariables, PathVariable pathVariable, String parameterName) {
        String value = pathVariable.value();
        if (StringUtils.hasText(value)) {
            return uriVariables.get(value);
        }

        return uriVariables.get(parameterName);
    }
}
