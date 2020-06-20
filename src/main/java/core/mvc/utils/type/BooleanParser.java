package core.mvc.utils.type;

public class BooleanParser implements TypeParser {
    @Override
    public boolean supports(Class<?> type) {
        return boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type);
    }

    @Override
    public Object convert(String raw) {
        return Boolean.parseBoolean(raw);
    }
}
