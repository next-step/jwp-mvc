package core.mvc.tobe.handler.resolver.utils;

public class TypeUtils {
    public static boolean isSimpleType(Class<?> type) {
        return type.isPrimitive() ||
                Number.class.isAssignableFrom(type) ||
                String.class.isAssignableFrom(type) ||
                Boolean.class.isAssignableFrom(type);
    }
}
