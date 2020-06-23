package core.mvc.support;

import javax.servlet.http.HttpServletRequest;

public class RequestParamResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportParameter(MethodParameter parameter) {
        return parameter.isSameClass(String.class) ||
                parameter.isSameClass(int.class) ||
                parameter.isSameClass(long.class);
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpServletRequest request) {
        Object value = request.getParameter(parameter.getName());

        if (parameter.isSameClass(int.class)) {
            return Integer.valueOf((int)value);
        }

        if (parameter.isSameClass(long.class)) {
            return Long.valueOf((long)value);
        }

        return value;
    }
}
