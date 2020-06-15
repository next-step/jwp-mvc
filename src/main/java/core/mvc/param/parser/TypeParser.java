package core.mvc.param.parser;

import java.util.Arrays;
import java.util.List;

public class TypeParser {
    private static final List<Parser> PARSERS = Arrays.asList(
            new IntParser(),
            new LongParser(),
            new StringParser()
    );

    private TypeParser() {}

    public static <T> T parse(Class<T> type, String value) {
        return PARSERS.stream()
                .filter(parser -> parser.isParsable(type))
                .findFirst()
                .map(parser -> (T) parser.parse(value))
                .orElse((T) value);
    }
}
