package core.mvc.tobe.handler.resolver;

import core.mvc.tobe.handler.resolver.utils.SimpleTypeConverter;
import core.mvc.tobe.handler.resolver.utils.TypeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class SimpleTypeRequestParameterArgumentResolver implements ArgumentResolver {

    @Override
    public boolean support(NamedParameter parameter) {
        return TypeUtils.isSimpleType(parameter.getType());
    }

    @Override
    public Object resolve(NamedParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        String parameterName = parameter.getName();
        String value = request.getParameter(parameterName);
        if (Objects.isNull(value)) {
            throw new ArgumentResolveFailException(String.format("requestParameter - [%s] 값이 null입니다.", parameterName));
        }
        Object convertedValue = new SimpleTypeConverter().convert(value, parameter.getType());
        return convertedValue;
    }
}
