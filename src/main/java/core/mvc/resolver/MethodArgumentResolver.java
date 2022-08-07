package core.mvc.resolver;


import core.mvc.MethodParameter;
import core.mvc.exception.ArgumentResolverException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MethodArgumentResolver {

    boolean support(MethodParameter parameter);

    Object resolve(HttpServletRequest request, HttpServletResponse response, String parameterName, MethodParameter parameter) throws ArgumentResolverException;

    default Object cast(Class<?> clazz, String value) {
        if (Boolean.class == clazz || boolean.class == clazz) {
            return Boolean.parseBoolean(value);
        }
        if (Byte.class == clazz || byte.class == clazz) {
            return Byte.parseByte(value);
        }
        if (Short.class == clazz || short.class == clazz) {
            return Short.parseShort(value);
        }
        if (Integer.class == clazz || int.class == clazz) {
            return Integer.parseInt(value);
        }
        if (Long.class == clazz || long.class == clazz) {
            return Long.parseLong(value);
        }
        if (Float.class == clazz || float.class == clazz) {
            return Float.parseFloat(value);
        }
        if (Double.class == clazz || double.class == clazz) {
            return Double.parseDouble(value);
        }
        return value;
    }
}
