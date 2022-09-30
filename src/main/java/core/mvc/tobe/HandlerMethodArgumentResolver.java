package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMethodArgumentResolver {
    Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request);

    boolean supportsParameter(MethodParameter methodParameter);
}
