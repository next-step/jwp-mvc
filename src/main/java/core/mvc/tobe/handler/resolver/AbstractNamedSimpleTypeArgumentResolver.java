package core.mvc.tobe.handler.resolver;

import core.mvc.tobe.handler.resolver.utils.SimpleTypeConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

abstract public class AbstractNamedSimpleTypeArgumentResolver implements ArgumentResolver {
    private final SimpleTypeConverter simpleTypeConverter;

    public AbstractNamedSimpleTypeArgumentResolver(SimpleTypeConverter simpleTypeConverter) {
        this.simpleTypeConverter = simpleTypeConverter;
    }

    @Override
    public Object resolve(NamedParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        String parameterName = getParameterName(parameter);
        String value = getNamedValue(parameter, request, parameterName);
        if (Objects.isNull(value)) {
            throw new ArgumentResolveFailException(String.format("requestParameter - [%s] 값이 null입니다.", parameterName));
        }
        return simpleTypeConverter.convert(value, parameter.getType());
    }

    protected abstract String getParameterName(NamedParameter namedParameter);

    protected abstract String getNamedValue(NamedParameter namedParameter, HttpServletRequest request, String parameterName);
}
