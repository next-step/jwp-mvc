package core.mvc.tobe.method;

import java.util.Map;
import java.util.function.Function;

public class SimpleTypeConverter {

    private SimpleTypeConverter() {
    }

    private static class SimpleTypeConverterHolder {
        private static final SimpleTypeConverter TYPE_CONVERTER = new SimpleTypeConverter();
    }

    private static final Map<Class<?>, Function<String, ?>> SUPPORT_CLASSES = Map.of(
            Integer.TYPE, Integer::parseInt,
            Integer.class, Integer::parseInt,
            Long.TYPE, Long::parseLong,
            Long.class, Long::parseLong,
            String.class, String::toString
    );

    public static SimpleTypeConverter getTypeConverter() {
        return SimpleTypeConverterHolder.TYPE_CONVERTER;
    }

    public boolean isSupports(Class<?> clazz) {
        return SUPPORT_CLASSES.containsKey(clazz);
    }

    public Object convert(String input, Class<?> type) {
        if (!isSupports(type)) {
            throw new IllegalArgumentException(String.format("%s는 %s로 변환될 수 없습니다.", input, type));
        }
        return SUPPORT_CLASSES.get(type).apply(input);
    }
}
