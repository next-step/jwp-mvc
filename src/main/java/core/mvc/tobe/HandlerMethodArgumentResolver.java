package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodArgumentResolver {

    boolean support(MethodParameter methodParameter);

    Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response);
}
