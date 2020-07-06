package core.mvc.tobe.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class SessionArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supportsParameter(Parameter parameter) {
        return HttpSession.class.equals(parameter.getType());
    }

    @Override
    public Object resolve(HttpServletRequest request, Method method, Parameter parameter, int index) {
        return request.getSession();
    }
}
