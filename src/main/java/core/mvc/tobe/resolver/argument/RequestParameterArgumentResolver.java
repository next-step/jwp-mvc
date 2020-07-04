package core.mvc.tobe.resolver.argument;

import javax.servlet.http.HttpServletRequest;

public class RequestParameterArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean isSupport(MethodParameter methodParameter) {
        return methodParameter.isRequestParameterArgument();
    }

    @Override
    public Object resolve(HttpServletRequest request, MethodParameter methodParameter) {
        String parameterValue = request.getParameter(methodParameter.getParameterName());
        Object value = methodParameter.getParameterTypeValue(parameterValue);

        return value;
    }
}
