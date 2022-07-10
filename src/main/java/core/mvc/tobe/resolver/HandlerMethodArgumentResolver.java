package core.mvc.tobe.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(Parameter parameter);

    @Nullable
    Object resolveArgument(Parameter parameter, String parameterName, HttpServletRequest httpServletRequest);
}
