package core.mvc.support;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMethodArgumentResolver {

    boolean supportParameter(MethodParameter parameter);

    Object resolve(MethodParameter parameter, HttpServletRequest request);

}
