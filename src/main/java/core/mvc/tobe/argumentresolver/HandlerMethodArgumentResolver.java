package core.mvc.tobe.argumentresolver;

import core.mvc.tobe.MethodParameter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodArgumentResolver {

    Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response);

    boolean supportsParameter(MethodParameter methodParameter);

}
