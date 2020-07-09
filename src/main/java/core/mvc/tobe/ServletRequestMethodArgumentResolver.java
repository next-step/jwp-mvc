package core.mvc.tobe;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return ServletRequest.class.isAssignableFrom(methodParameter.getType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return req;
    }
}
