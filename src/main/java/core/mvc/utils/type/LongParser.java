package core.mvc.utils.type;

public class LongParser implements TypeParser {
    @Override
    public boolean supports(Class<?> type) {
        return long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type);
    }

    @Override
    public Object convert(String raw) {
        return Long.parseLong(raw);
    }
}
