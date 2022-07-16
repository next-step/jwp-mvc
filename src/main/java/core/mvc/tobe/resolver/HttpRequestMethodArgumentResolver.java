package core.mvc.tobe.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HttpRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(Parameter parameter) {
        if (parameter.getType().equals(HttpServletRequest.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(Parameter parameter, String parameterName, Method method, HttpServletRequest request, HttpServletResponse response) {
        return request;
    }
}
