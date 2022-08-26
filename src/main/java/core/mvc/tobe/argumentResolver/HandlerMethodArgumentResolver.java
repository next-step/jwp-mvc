package core.mvc.tobe.argumentResolver;

import java.util.List;

public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(Class<?> parameterType);

    Object resolveArgument(Class<?> parameterType, List<Object> argument);
}
