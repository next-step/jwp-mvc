package core.mvc.tobe.resolver.argument;

import javax.servlet.http.HttpServletRequest;

public class PathVariableArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean isSupport(MethodParameter methodParameter) {
        return methodParameter.isPathVariableArgument();
    }

    @Override
    public Object resolve(HttpServletRequest request, MethodParameter methodParameter) {
        String uri = methodParameter.getMethodRequestMappingValue();
        int startIndex = uri.indexOf("{" + methodParameter.getParameterName() + "}");
        int endIndex = request.getRequestURI().indexOf("/", startIndex);

        String str = request.getRequestURI().substring(startIndex);
        if (endIndex >= 0) {
            str = request.getRequestURI().substring(startIndex, endIndex);
        }

        Object value = methodParameter.getParameterTypeValue(str);
        return value;
    }
}
