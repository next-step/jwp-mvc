package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public interface HandlerMethodArgumentResolver {

    boolean support(Method method);

    Object[] resolve(Method method, HttpServletRequest request, HttpServletResponse response);
}
