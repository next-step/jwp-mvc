package core.mvc.tobe.resolver;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMethodArgumentResolver {

    boolean isSupport(Class<?> parameterType);

    Object resolve(HttpServletRequest request, String parameterName, Class<?> parameterType);
}
