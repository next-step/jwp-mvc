package core.mvc.utils.type;

public class StringParser implements TypeParser {
    @Override
    public boolean supports(Class<?> type) {
        return String.class.isAssignableFrom(type);
    }

    @Override
    public Object convert(String raw) {
        return raw;
    }
}
