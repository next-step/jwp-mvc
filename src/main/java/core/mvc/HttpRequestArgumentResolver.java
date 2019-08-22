package core.mvc;

import core.di.factory.MethodParameter;

import javax.servlet.http.HttpServletRequest;

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
