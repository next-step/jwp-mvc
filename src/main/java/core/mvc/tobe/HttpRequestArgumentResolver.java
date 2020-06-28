package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpRequestArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean support(MethodParameter methodParameter) {
        return methodParameter.isHttpServletRequest();
    }

    @Override
    public Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return request;
    }

}
