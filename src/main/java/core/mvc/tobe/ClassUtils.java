package core.mvc.tobe;

import java.util.HashMap;
import java.util.Map;

public class ClassUtils {

    private static final Map<Class<?>, Class<?>> primitiveWrapperTypes = new HashMap<>();

    static {
        primitiveWrapperTypes.put(Integer.class, int.class);
        primitiveWrapperTypes.put(Long.class, long.class);
    }

    private ClassUtils() {
    }

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() || isPrimitiveWrapper(type);
    }

    private static boolean isPrimitiveWrapper(Class<?> type) {
        return primitiveWrapperTypes.containsKey(type);
    }
}
