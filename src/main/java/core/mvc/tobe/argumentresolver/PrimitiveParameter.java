package core.mvc.tobe.argumentresolver;

import java.util.Arrays;
import java.util.function.Function;

public enum PrimitiveParameter {

    INTEGER(int.class, Integer.class, 0, Integer::parseInt),
    LONG(long.class, Long.class, 0L, Long::parseLong),
    STRING(String.class, String.class, null, s -> s);

    private final Class<?> primitiveType;
    private final Class<?> wrapperType;
    private final Object defaultValue;
    private final Function<String, Object> parseFunction;

    PrimitiveParameter(final Class<?> primitiveType, final Class<?> wrapperType, final Object defaultValue, final Function<String, Object> parseFunction) {
        this.primitiveType = primitiveType;
        this.wrapperType = wrapperType;
        this.defaultValue = defaultValue;
        this.parseFunction = parseFunction;
    }

    public static PrimitiveParameter from(Class<?> parameterType) {
        return Arrays.stream(values())
            .filter(parameter -> parameter.primitiveType == parameterType || parameter.wrapperType == parameterType)
            .findAny()
            .orElseThrow(IllegalArgumentException::new);
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public Object convert(String value) {
        return parseFunction.apply(value);
    }

}
