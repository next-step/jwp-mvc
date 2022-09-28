package core.mvc.resolver.util;

public class ParameterUtils {
    public static Object getObjectByParameterType(Object value, Class<?> parameterType) {
        if (parameterType.equals(String.class)) {
            return value.toString();
        }
        if (parameterType.equals(Integer.class)) {
            return Integer.valueOf(value.toString());
        }
        if (parameterType.equals(int.class)) {
            return Integer.parseInt(value.toString());
        }
        if (parameterType.equals(long.class)) {
            return Long.parseLong(value.toString());
        }
        throw new RuntimeException("Invalid parameter type.");
    }
}
