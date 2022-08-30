package core.mvc.tobe.utils;

public interface TypeUtils {
    static Object stringToType(Class<?> targetType, String value) {
        if (targetType.equals(Long.class) || targetType.equals(long.class)) {
            return Long.parseLong(value);
        }

        if (targetType.equals(Integer.class) || targetType.equals(int.class)) {
            return Integer.parseInt(value);
        }

        return value;
    }
}
