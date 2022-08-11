package core.mvc.tobe.method;

import java.util.Map;
import java.util.function.Function;

class SimpleTypeConverter {

    private static final SimpleTypeConverter INSTANCE = new SimpleTypeConverter();
    private static final Map<Class<?>, Function<String, ?>> SUPPORTED_CLASSES = Map.of(
            Integer.TYPE, Integer::parseInt,
            Integer.class, Integer::parseInt,
            Long.TYPE, Long::parseLong,
            Long.class, Long::parseLong,
            String.class, String::toString
    );

    private SimpleTypeConverter() {
        if (INSTANCE != null) {
            throw new AssertionError(String.format("%s can not be instanced", getClass()));
        }
    }

    static SimpleTypeConverter instance() {
        return INSTANCE;
    }

    boolean isSupports(Class<?> clazz) {
        return SUPPORTED_CLASSES.containsKey(clazz);
    }

    Object convert(String string, Class<?> type) {
        if (!isSupports(type)) {
            throw new IllegalStateException(String.format("string(%s) can not be converted by type(%s)", string, type));
        }
        return SUPPORTED_CLASSES.get(type)
                .apply(string);
    }
}
