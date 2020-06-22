package core.mvc.utils.type;

public class ByteParser implements TypeParser {
    @Override
    public boolean supports(Class<?> type) {
        return byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type);
    }

    @Override
    public Object convert(String raw) {
        return Byte.parseByte(raw);
    }
}
