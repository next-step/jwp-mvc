package core.mvc.tobe.resolver;

import java.util.Map;
import java.util.function.Function;

public class TypeConverter {
    private static final Map<Class<?>, Function<String, ?>> converters = Map.of(
            int.class, Integer::parseInt,
            Integer.class, Integer::parseInt,
            long.class, Long::parseLong,
            Long.class, Long::parseLong,
            boolean.class, Boolean::parseBoolean,
            Boolean.class, Boolean::parseBoolean,
            double.class, Double::parseDouble,
            Double.class, Double::parseDouble,
            String.class, String::toString
    );

    private TypeConverter() {
    }

    public static Object convert(Class<?> type, String value) {
        if (!converters.containsKey(type)) {
            throw new IllegalArgumentException(String.format("해당 타입(값 : %s)은 변환을 지원하지 않습니다. 변환 타입 : %s", value, type));
        }

        return converters.get(type).apply(value);
    }
}
