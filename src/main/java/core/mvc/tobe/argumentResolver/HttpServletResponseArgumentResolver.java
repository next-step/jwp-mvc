package core.mvc.tobe.argumentResolver;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

public class HttpServletResponseArgumentResolver extends AbstractArgumentResolver {

    @Override
    public boolean supportsParameter(Class<?> parameterType) {
        return parameterType.equals(HttpServletResponse.class);
    }

    @Override
    public Object resolveArgument(Class<?> parameterType, List<Object> arguments) {
        return arguments.stream().filter(Objects::nonNull)
                .filter(argument -> argument.getClass().equals(parameterType)
                        || argument.getClass().getSimpleName().equals("MockHttpServletResponse"))
                .findFirst().orElse(null);
    }
}
