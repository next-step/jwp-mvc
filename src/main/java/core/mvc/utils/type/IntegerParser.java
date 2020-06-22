package core.mvc.utils.type;

public class IntegerParser implements TypeParser {
    @Override
    public boolean supports(Class<?> type) {
        return int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type);
    }

    @Override
    public Object convert(String raw) {
        return Integer.parseInt(raw);
    }
}
