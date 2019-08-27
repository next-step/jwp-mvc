package core.resolver;

import javax.servlet.http.HttpServletRequest;

import core.di.factory.MethodParameter;
import core.mvc.WebRequest;

public class HttpRequestArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return methodParameter.getType().isAssignableFrom(HttpServletRequest.class);
    }

    @Override
    public Object resolve(MethodParameter methodParameter, WebRequest webRequest) {
        return webRequest.getRequest();
    }
}
