package core.mvc.resolver;

import core.annotation.Component;
import core.mvc.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class SessionArgumentResolver implements MethodArgumentResolver{
    @Override
    public boolean support(MethodParameter parameter) {
        return parameter.getParameterType().equals(HttpSession.class);
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, String parameterName, MethodParameter parameter) {
        return request.getSession();
    }
}
