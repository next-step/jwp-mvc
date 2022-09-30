package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public class DefaultMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) {
        return request.getParameter(methodParameter.getParameterName());
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return true;
    }
}
