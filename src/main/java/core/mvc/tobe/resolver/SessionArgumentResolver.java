package core.mvc.tobe.resolver;



import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionArgumentResolver implements ArgumentResolver{
    @Override
    public boolean support(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(HttpSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  String parameterName) {
        return request.getSession();
    }
}
