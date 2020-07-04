package core.mvc.tobe.resolver.argument;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMethodArgumentResolver {

    boolean isSupport(MethodParameter methodParameter);

    Object resolve(HttpServletRequest request, MethodParameter methodParameter);
}
