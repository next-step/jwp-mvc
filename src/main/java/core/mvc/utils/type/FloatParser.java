package core.mvc.utils.type;

public class FloatParser implements TypeParser {
    @Override
    public boolean supports(Class<?> type) {
        return float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type);
    }

    @Override
    public Object convert(String raw) {
        return Float.parseFloat(raw);
    }
}
