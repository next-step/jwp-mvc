package core.mvc.tobe.resolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMethodArgumentResolver {
    boolean supportsParameter(Parameter parameter);

    Object resolve(String parameterName, Parameter parameter, HttpServletRequest request, Method method);
}
