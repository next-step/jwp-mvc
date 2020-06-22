package core.mvc.utils.type;

public class ShortParser implements TypeParser {
    @Override
    public boolean supports(Class<?> type) {
        return short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type);
    }

    @Override
    public Object convert(String raw) {
        return Short.parseShort(raw);
    }
}
