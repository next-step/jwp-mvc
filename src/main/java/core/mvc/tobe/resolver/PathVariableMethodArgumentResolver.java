package core.mvc.tobe.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import next.support.PathPatternUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.util.StringUtils;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
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
    public Object resolveArgument(Parameter parameter, String parameterName, Method method, HttpServletRequest httpServletRequest) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String path = requestMapping.value();
        Map<String, String> uriVariables = PathPatternUtils.parse(path)
                                                           .matchAndExtract(PathPatternUtils.toPathContainer(httpServletRequest.getRequestURI()))
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
