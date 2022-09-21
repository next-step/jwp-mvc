package core.mvc.resolver;

import core.annotation.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SessionArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(HttpSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        return request.getSession();
    }
}
