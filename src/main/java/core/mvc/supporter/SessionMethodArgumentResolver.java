package core.mvc.supporter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class SessionMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(Parameter parameter) {
        return HttpSession.class.equals(parameter.getType());
    }

    @Override
    public Object resolveArgument(Parameter parameter, Method method, HttpServletRequest httpServletRequest, int index) throws Exception {
        return httpServletRequest.getSession();
    }
}
