package core.mvc.resolver;

import java.util.Objects;
import java.util.function.Function;

public enum PrimitiveType {

    INT(int.class, Integer.class, Integer::parseInt),
    LONG(long.class, Long.class, Long::parseLong),
    DOUBLE(double.class, Double.class, Double::parseDouble),
    FLOAT(float.class, Float.class, Float::parseFloat),
    BOOLEAN(boolean.class, Boolean.class, Boolean::parseBoolean),
    BYTE(byte.class, Byte.class, Byte::parseByte),
    SHORT(short.class, Short.class, Short::parseShort),
    CHAR(char.class, Character.class, String::chars),
    STRING(String.class, String.class, x -> x);

    private final Class<?> primitiveType;
    private final Class<?> wrapperType;
    private final Function<String, Object> parser;

    PrimitiveType(Class<?> primitiveType, Class<?> wrapperType, Function<String, Object> parser) {
        this.primitiveType = primitiveType;
        this.wrapperType = wrapperType;
        this.parser = parser;
    }

    private static PrimitiveType find(Class<?> type) {
        for (PrimitiveType primitiveType : values()) {
            if (primitiveType.primitiveType.equals(type) || primitiveType.wrapperType.equals(type)) {
                return primitiveType;
            }
        }
        return null;
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return Objects.nonNull(find(clazz));
    }

    public static Object convert(Class<?> parameterType, String requestParameter) {
        return Objects.requireNonNull(find(parameterType))
                .parser.apply(requestParameter);
    }
}
