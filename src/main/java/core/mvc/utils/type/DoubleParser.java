package core.mvc.utils.type;

public class DoubleParser implements TypeParser {
    @Override
    public boolean supports(Class<?> type) {
        return double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type);
    }

    @Override
    public Object convert(String raw) {
        return Double.parseDouble(raw);
    }
}
