package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpResponseArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean support(MethodParameter methodParameter) {
        return methodParameter.isHttpServletResponse();
    }

    @Override
    public Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return response;
    }

}
