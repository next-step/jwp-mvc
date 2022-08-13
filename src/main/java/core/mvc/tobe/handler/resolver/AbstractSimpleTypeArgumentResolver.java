package core.mvc.tobe.handler.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public abstract class AbstractSimpleTypeArgumentResolver implements ArgumentResolver {

    @Override
    public Object resolve(NamedParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        String parameterName = parameter.getName();
        String value = request.getParameter(parameterName);
        if (Objects.nonNull(value)) {
            return resolveInternal(value);
        }
        throw new ArgumentResolveFailException(String.format("requestParameter - [%s] 값이 null입니다.", parameterName));
    }

    abstract Object resolveInternal(String value);
}
