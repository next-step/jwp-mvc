package core.mvc.tobe.argumentresolver;

import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String USER_ID_PARAMETER_NAME = "userId";
    private static final String USER_PASSWORD_PARAMETER_NAME = "password";
    private static final String USER_NAME_PARAMETER_NAME = "name";
    private static final String USER_EMAIL_PARAMETER_NAME = "email";

    @Override
    public Object resolveArgument(HttpServletRequest request, HttpServletResponse response, Parameter parameter, String parameterName) {
        return new User(request.getParameter(USER_ID_PARAMETER_NAME),
                request.getParameter(USER_PASSWORD_PARAMETER_NAME),
                request.getParameter(USER_NAME_PARAMETER_NAME),
                request.getParameter(USER_EMAIL_PARAMETER_NAME));
    }

    @Override
    public boolean supportsParameter(Parameter parameter) {
        Class<?> parameterType = parameter.getType();
        return parameterType.equals(User.class);
    }
}
