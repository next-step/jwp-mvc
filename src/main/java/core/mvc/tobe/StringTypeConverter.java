package core.mvc.tobe;

public class StringTypeConverter implements ValueTypeConverter {

    @Override
    public boolean supports(Class<?> type) {
        return type == String.class;
    }

    @Override
    public <T> T convert(Class<T> type, String value) {
        return (T) value;
    }
}
