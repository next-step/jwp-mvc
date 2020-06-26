package core.mvc.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodArgumentResolver {

    boolean supportParameter(MethodParameter parameter);

    Object resolve(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response);

}
