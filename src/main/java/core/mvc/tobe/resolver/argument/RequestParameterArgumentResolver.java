package core.mvc.tobe.resolver.argument;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestParameterArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean isSupport(MethodParameter methodParameter) {
        return methodParameter.isRequestParameterArgument();
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, MethodParameter methodParameter) {
        String parameterValue = request.getParameter(methodParameter.getParameterName());
        Object value = methodParameter.getParameterTypeValue(parameterValue);

        return value;
    }
}
