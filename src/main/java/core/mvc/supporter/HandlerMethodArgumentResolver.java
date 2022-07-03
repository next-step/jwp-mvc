package core.mvc.supporter;

import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(Parameter parameter);

    @Nullable
    Object resolveArgument(Parameter parameter, Method method,
                           HttpServletRequest httpServletRequest, int index) throws Exception;
}
