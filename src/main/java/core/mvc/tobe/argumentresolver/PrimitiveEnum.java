package core.mvc.tobe.argumentresolver;

import java.util.Arrays;
import java.util.function.Function;

public enum PrimitiveEnum {
    INTEGER(int.class, Integer::parseInt),
    LONG(long.class, Long::parseLong),
    FLOAT(float.class, Float::parseFloat),
    DOUBLE(double.class, Double::parseDouble);

    private Class<?> type;
    private Function<String, Object> numberConvert;

    PrimitiveEnum(Class<?> type, Function<String, Object> numberConvert) {
        this.type = type;
        this.numberConvert = numberConvert;
    }

    public static boolean isPrimitiveType(Class<?> type) {
        return Arrays.stream(PrimitiveEnum.values())
                .anyMatch(primitiveEnum -> primitiveEnum.type.equals(type));
    }

    public static Object convert(Class<?> type, String value) {
        return Arrays.stream(PrimitiveEnum.values())
                .filter(primitiveEnum -> primitiveEnum.type.equals(type))
                .map(primitiveEnum -> primitiveEnum.numberConvert.apply(value))
                .findFirst()
                .orElse(0);
    }
}
