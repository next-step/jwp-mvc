package core.mvc.support;

import javax.servlet.http.HttpServletRequest;

public class ServletRequestResolver implements HandlerMethodArgumentResolver {

    private final HttpServletRequest request;

    public ServletRequestResolver(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public boolean supportParameter(MethodParameter parameter) {
        final boolean isRequest = parameter.matchClass(HttpServletRequest.class);
        return isRequest;
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpServletRequest request) {
        return request;
    }
}
