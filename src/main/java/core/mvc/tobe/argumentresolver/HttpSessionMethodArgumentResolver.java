package core.mvc.tobe.argumentresolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpSessionMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean resolvable(final MethodParameter methodParameter) {
        return HttpSession.class == methodParameter.getParameterType();
    }

    @Override
    public Object resolve(final MethodParameter methodParameter, final HttpServletRequest request) {
        return request.getSession();
    }
}
