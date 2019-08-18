package core.mvc.tobe.argumentresolver;

import next.model.User;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public Object resolveArgument(HttpServletRequest request, Parameter parameter, String parameterName) {
        return new User(request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email"));
    }

    @Override
    public boolean supportsParameter(Parameter parameter) {
        Class<?> parameterType = parameter.getType();
        return parameterType.equals(User.class);
    }
}
