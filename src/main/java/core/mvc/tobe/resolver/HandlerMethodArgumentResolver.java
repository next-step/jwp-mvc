package core.mvc.tobe.resolver;

import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(Parameter parameter);

    @Nullable
    Object resolveArgument(Parameter parameter, String parameterName, Method method, HttpServletRequest request, HttpServletResponse response);
}
