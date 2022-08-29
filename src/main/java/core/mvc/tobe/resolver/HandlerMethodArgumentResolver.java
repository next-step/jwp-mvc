package core.mvc.tobe.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

public interface HandlerMethodArgumentResolver {
    boolean supportsParameter(MethodParameter methodParameter);

    Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, InstantiationException, IllegalAccessException;
}
