package core.mvc.tobe.resolver;


import core.mvc.tobe.resolver.method.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(MethodParameter parameter);

    Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) ;
}
