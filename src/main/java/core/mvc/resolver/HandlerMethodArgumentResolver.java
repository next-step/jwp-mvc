package core.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(MethodParameter methodParameter);

    Object resolve(MethodParameter methodParameter, String mappingUrl, HttpServletRequest httpRequest, HttpServletResponse httpResponse);
}
