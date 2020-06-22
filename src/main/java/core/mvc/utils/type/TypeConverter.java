package core.mvc.utils.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Converts string to primitive type
 *
 * @author hyeyoom
 */
public final class TypeConverter {

    private static final List<TypeParser> TYPE_PARSERS = new ArrayList<>();

    static {
        TYPE_PARSERS.add(new BooleanParser());
        TYPE_PARSERS.add(new ByteParser());
        TYPE_PARSERS.add(new CharacterParser());
        TYPE_PARSERS.add(new DoubleParser());
        TYPE_PARSERS.add(new FloatParser());
        TYPE_PARSERS.add(new IntegerParser());
        TYPE_PARSERS.add(new LongParser());
        TYPE_PARSERS.add(new ShortParser());
        TYPE_PARSERS.add(new StringParser());
    }

    public static Object convert(String raw, Class<?> type) {
        final Optional<TypeParser> maybeParser = TYPE_PARSERS.stream()
                .filter(typeParser -> typeParser.supports(type))
                .findFirst();
        if (maybeParser.isPresent()) {
            final TypeParser parser = maybeParser.get();
            return parser.convert(raw);
        }
        throw new IllegalArgumentException("Unsupported type detected: " + type);
    }
}
