package core.mvc.tobe.argumentresolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MethodArgumentResolver {
    boolean support(MethodParameter methodParameter);

    Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response);
}
