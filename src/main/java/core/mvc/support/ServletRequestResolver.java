package core.mvc.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRequestResolver implements HandlerMethodArgumentResolver {

    public ServletRequestResolver() {
    }

    @Override
    public boolean supportParameter(MethodParameter parameter) {
        final boolean isRequest = parameter.matchClass(HttpServletRequest.class);
        return isRequest;
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return request;
    }
}
