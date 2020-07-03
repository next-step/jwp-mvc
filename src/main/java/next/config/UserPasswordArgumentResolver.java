package next.config;

import core.mvc.argumentresolver.HandlerMethodArgumentResolver;
import core.mvc.argumentresolver.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserPasswordArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String PASSWORD_PARAMETER_KEY = "password";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.isAssignableFrom(String.class)
                && methodParameter.matchParameterName(PASSWORD_PARAMETER_KEY);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter(PASSWORD_PARAMETER_KEY);
    }
}
