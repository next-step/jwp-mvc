package core.mvc.tobe.resolver.argument;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface HandlerMethodArgumentResolver {

    boolean isSupport(Class<?> parameterType, Parameter parameter);

    Object resolve(HttpServletRequest request, Method method, String parameterName, Class<?> parameterType);
}
