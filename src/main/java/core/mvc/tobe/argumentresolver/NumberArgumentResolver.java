package core.mvc.tobe.argumentresolver;

import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.Objects;

public class NumberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(HttpServletRequest request, Parameter parameter, String parameterName) {
        String value = request.getParameter(parameterName);
        Class<?> parameterType = parameter.getType();

        if (Objects.isNull(value)) {
            return 0;
        }

        if (PrimitiveEnum.isPrimitiveType(parameterType)) {
            return PrimitiveEnum.convert(parameterType, value);
        }

        return parameterType.cast(NumberUtils.createNumber(value));
    }

    @Override
    public boolean supportsParameter(Parameter parameter) {
        Class<?> parameterType = parameter.getType();

        if (PrimitiveEnum.isPrimitiveType(parameterType)) {
            return true;
        }

        return parameterType.getSuperclass().equals(Number.class);
    }
}
