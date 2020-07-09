package core.mvc.tobe;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletResponseMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return ServletResponse.class.isAssignableFrom(methodParameter.getType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return resp;
    }
}
