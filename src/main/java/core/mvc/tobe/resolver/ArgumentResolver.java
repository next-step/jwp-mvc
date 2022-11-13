package core.mvc.tobe.resolver;





import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ArgumentResolver {
    boolean support(MethodParameter methodParameter);
    Object resolveArgument(MethodParameter methodParameter,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           String parameterName);

    default Object cast(Class<?> clazz, String value) {
        if (isByte(clazz)) {
            return Byte.parseByte(value);
        }

        if (isShort(clazz)) {
            return Short.parseShort(value);
        }

        if (isInteger(clazz)) {
            return Integer.parseInt(value);
        }

        if (isLong(clazz)) {
            return Long.parseLong(value);
        }

        if (isFloat(clazz)) {
            return Float.parseFloat(value);
        }
        if (isDouble(clazz)) {
            return Double.parseDouble(value);
        }

        if (isBoolean(clazz)) {
            return Boolean.parseBoolean(value);
        }

        return value;
    }

    private boolean isBoolean(Class<?> clazz) {
        return Boolean.class == clazz || boolean.class == clazz;
    }

    private boolean isDouble(Class<?> clazz) {
        return Double.class == clazz || double.class == clazz;
    }

    private boolean isFloat(Class<?> clazz) {
        return Float.class == clazz || float.class == clazz;
    }

    private boolean isLong(Class<?> clazz) {
        return Long.class == clazz || long.class == clazz;
    }

    private boolean isInteger(Class<?> clazz) {
        return Integer.class == clazz || int.class == clazz;
    }

    private boolean isByte(Class<?> clazz) {
        return Byte.class == clazz || byte.class == clazz;
    }

    private boolean isShort(Class<?> clazz) {
        return Short.class == clazz || short.class == clazz;
    }


}
