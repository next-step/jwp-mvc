package core.mvc.tobe;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public enum PrimitiveConvertor {

    BOOLEAN(Arrays.asList(boolean.class, Boolean.class), Boolean::valueOf),
    BYTE(Arrays.asList(byte.class, Byte.class), Byte::valueOf),
    CHARACTER(Arrays.asList(char.class, Character.class), (value) -> {return Character.valueOf(value.toCharArray()[0]);}),
    DOUBLE(Arrays.asList(double.class, Double.class), Double::valueOf),
    FLOAT(Arrays.asList(float.class, Float.class), Float::valueOf),
    INTERGER(Arrays.asList(int.class, Integer.class), Integer::valueOf),
    LONG(Arrays.asList(long.class, Long.class), Long::valueOf),
    SHORT(Arrays.asList(short.class, Short.class), Short::valueOf)
    ;

    private List<Class<?>> types;
    private Function<String, Object> convert;

    PrimitiveConvertor(List<Class<?>> types, Function<String, Object> convert) {
        this.types = types;
        this.convert = convert;
    }

    public static PrimitiveConvertor valueOf(final Class<?> value) {
        return Arrays.stream(values())
                .filter(type -> type.types.contains(value))
                .findFirst()
                .orElse(null);
    }

    public Object convert(final String value) {
        return convert.apply(value);
    }
}
