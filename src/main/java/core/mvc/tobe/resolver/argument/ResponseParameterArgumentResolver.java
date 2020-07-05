package core.mvc.tobe.resolver.argument;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResponseParameterArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean isSupport(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();
        return parameterType == HttpServletRequest.class || parameterType == HttpServletResponse.class;
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, MethodParameter methodParameter) {
        if (methodParameter.getParameterType() == HttpServletRequest.class) {
            return request;
        }
        if (methodParameter.getParameterType() == HttpServletResponse.class) {
            return response;
        }
        return null;
    }
}
