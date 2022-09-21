package core.mvc.resolver;

import core.annotation.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class HttpServletRequestArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(HttpServletRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        return request;
    }
}
