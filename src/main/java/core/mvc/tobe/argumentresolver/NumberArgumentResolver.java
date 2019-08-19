package core.mvc.tobe.argumentresolver;

import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.Optional;

public class NumberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(HttpServletRequest request, HttpServletResponse response, Parameter parameter, String parameterName) {
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

        return Optional.ofNullable(parameterType.getSuperclass())
                .flatMap(aClass -> Optional.of(aClass.equals(Number.class)))
                .orElse(false);

    }
}
