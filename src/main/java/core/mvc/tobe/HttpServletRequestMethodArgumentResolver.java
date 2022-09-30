package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) {
        Class<?> parameterType = methodParameter.getParameterType();
        String value = request.getParameter(methodParameter.getParameterName());

        if (parameterType.equals(HttpServletRequest.class)) {
            return request;
        }

        return value;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(HttpServletRequest.class);
    }
}
