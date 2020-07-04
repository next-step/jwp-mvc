package core.mvc.tobe.resolver.argument;

import core.mvc.tobe.utils.PathPatternUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class PathVariableArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean isSupport(MethodParameter methodParameter) {
        return methodParameter.isPathVariableArgument();
    }

    @Override
    public Object resolve(HttpServletRequest request, MethodParameter methodParameter) {
        String requestUri = request.getRequestURI();
        String uri = methodParameter.getMethodRequestMappingValue();
        Map<String, String> variables = PathPatternUtils.parse(uri)
                .matchAndExtract(PathPatternUtils.toPathContainer(requestUri))
                .getUriVariables();

        String pathValue = variables.get(methodParameter.getParameterName());

        Object value = methodParameter.getParameterTypeValue(pathValue);
        return value;
    }
}
