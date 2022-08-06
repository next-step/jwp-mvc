package core.mvc.tobe;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public enum TypeConverter {
    INTEGER(List.of(Integer.class, int.class), Integer::parseInt),
    LONG(List.of(Long.class, long.class), Long::parseLong)
    ;

    private final List<Class<?>> types;
    private final Function<String, ?> function;

    TypeConverter(List<Class<?>> types, Function<String, ?> function) {
        this.types = types;
        this.function = function;
    }

    public static TypeConverter valueOf(Class<?> type) {
        return Arrays.stream(values())
            .filter(converter -> converter.types.contains(type))
            .findFirst()
            .orElseThrow(IllegalStateException::new);
    }

    public Object convert(String value) {
        return function.apply(value);
    }
}
