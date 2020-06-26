package core.mvc.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletResponseResolver implements HandlerMethodArgumentResolver {

    public ServletResponseResolver() {}

    @Override
    public boolean supportParameter(MethodParameter parameter) {
        final boolean isResponse = parameter.matchClass(HttpServletResponse.class);
        return isResponse;
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return response;
    }
}
