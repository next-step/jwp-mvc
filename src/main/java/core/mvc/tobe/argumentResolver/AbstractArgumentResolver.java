package core.mvc.tobe.argumentResolver;

import java.util.List;
import java.util.Objects;

public abstract class AbstractArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(Class<?> parameterType, List<Object> arguments) {
        return arguments.stream().filter(Objects::nonNull).findFirst().orElse(null);
    }
}
