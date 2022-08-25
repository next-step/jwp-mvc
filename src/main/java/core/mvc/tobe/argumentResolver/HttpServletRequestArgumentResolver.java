package core.mvc.tobe.argumentResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class HttpServletRequestArgumentResolver extends AbstractArgumentResolver {

    @Override
    public boolean supportsParameter(Class<?> parameterType) {
        return parameterType.equals(HttpServletRequest.class);
    }

    @Override
    public Object resolveArgument(Class<?> parameterType, List<Object> arguments) {
        return arguments.stream().filter(Objects::nonNull)
                .filter(argument -> argument.getClass().equals(parameterType)
                        || argument.getClass().getSimpleName().equals("MockHttpServletRequest"))
                .findFirst().orElse(null);
    }
}
