package core.mvc.tobe.argumentresolver;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean resolvable(final MethodParameter methodParameter) {
        return HttpServletRequest.class == methodParameter.getParameterType();
    }

    @Override
    public Object resolve(final MethodParameter methodParameter, final HttpServletRequest request) {
        return null;
    }
}
