package core.resolver;

import javax.servlet.http.HttpServletResponse;

import core.di.factory.MethodParameter;
import core.mvc.WebRequest;

public class HttpResponseArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return methodParameter.getType().isAssignableFrom(HttpServletResponse.class);
    }

    @Override
    public Object resolve(MethodParameter methodParameter, WebRequest webRequest) {
        return webRequest.getResponse();
    }
}
