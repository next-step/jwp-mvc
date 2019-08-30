package core.mvc.tobe;

import java.util.HashMap;
import java.util.Map;

public class TypeDescriptor {

    private static final Map<Class<?>, TypeDescriptor> commonTypesCache = new HashMap<>(32);

    private static final Class<?> [] CACHED_COMMON_TYPES = {
            boolean.class, Boolean.class,
            byte.class, Byte.class,
            char.class, Character.class,
            double.class, Double.class,
            float.class, Float.class,
            int.class, Integer.class,
            long.class, Long.class,
            short.class, Short.class,
            String.class,
            Object.class
    };

    static {
        for (Class<?> preCachedClass : CACHED_COMMON_TYPES) {
            commonTypesCache.put(preCachedClass, valueOf(preCachedClass));
        }
    }

    private final Class<?> type;
    private final PrimitiveConvertor primitiveConvertor;

    private TypeDescriptor(Class<?> type) {
        this.type = type;
        this.primitiveConvertor = PrimitiveConvertor.valueOf(type);
    }

    public static TypeDescriptor valueOf(Class<?> type) {
        if (type == null) {
            type = Object.class;
        }

        TypeDescriptor typeDescriptor = commonTypesCache.get(type);

        return typeDescriptor != null ? typeDescriptor : new TypeDescriptor(type);
    }

    public Object convertStringTo(final String value) {
        if (type.isPrimitive()) {
            return primitiveConvertor.convert(value);
        }

        return type.cast(value);
    }

    public Class<?> getType() {
        return type;
    }

    public boolean equals(Class<?> type) {
        return this.type.equals(type);
    }
}
