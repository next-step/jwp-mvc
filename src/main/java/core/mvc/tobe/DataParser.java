package core.mvc.tobe;

import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DataParser {

    STRING_TYPE((type -> type.equals(String.class)), (target -> target)),
    INTEGER_TYPE((type -> type.equals(int.class) || type.equals(Integer.class)), Integer::parseInt),
    LONG_TYPE((type -> type.equals(long.class) || type.equals(Long.class)), Long::parseLong),
    BOOLEAN_TYPE((type -> type.equals(boolean.class) || type.equals(Boolean.class)), Boolean::parseBoolean);

    private final Predicate<Class<?>> supports;
    private final Function<String, Object> parseFunction;

    DataParser(Predicate<Class<?>> supports, Function<String, Object> parseFunction) {
        this.supports = supports;
        this.parseFunction = parseFunction;
    }

    public Object parse(String target) {
        return this.parseFunction.apply(target);
    }

    private static EnumSet<DataParser> dataParsers = EnumSet.allOf(DataParser.class);

    public static DataParser from(Class<?> type) {
        return dataParsers.stream()
                .filter(dataParser -> dataParser.supports.test(type))
                .findFirst()
                .orElseGet(() -> STRING_TYPE);
    }

    public static boolean supports(Class<?> type) {
        return dataParsers.stream()
                .anyMatch(dataParser -> dataParser.supports.test(type));
    }
}
