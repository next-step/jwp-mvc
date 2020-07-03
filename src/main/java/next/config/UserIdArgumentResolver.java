package next.config;

import core.mvc.argumentresolver.HandlerMethodArgumentResolver;
import core.mvc.argumentresolver.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String USER_ID_PARAMETER_KEY = "userId";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.isAssignableFrom(String.class)
                && methodParameter.matchParameterName(USER_ID_PARAMETER_KEY);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter(USER_ID_PARAMETER_KEY);
    }
}
