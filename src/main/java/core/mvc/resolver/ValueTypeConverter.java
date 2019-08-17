package core.mvc.resolver;

public interface ValueTypeConverter {

    boolean supports(Class<?> type);

    <T> T convert(Class<T> type, String value);
}
