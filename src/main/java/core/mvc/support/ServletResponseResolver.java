package core.mvc.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletResponseResolver implements HandlerMethodArgumentResolver {

    private final HttpServletResponse response;

    public ServletResponseResolver(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public boolean supportParameter(MethodParameter parameter) {
        boolean isResponse = parameter.isSameClass(HttpServletResponse.class);
        return isResponse;
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpServletRequest request) {
        return response;
    }
}
