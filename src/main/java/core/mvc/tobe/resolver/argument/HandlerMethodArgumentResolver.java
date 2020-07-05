package core.mvc.tobe.resolver.argument;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodArgumentResolver {

    boolean isSupport(MethodParameter methodParameter);

    Object resolve(HttpServletRequest request, HttpServletResponse response, MethodParameter methodParameter);
}
