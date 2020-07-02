package core.mvc.argumentresolver.converter;

public interface ParameterTypeConverter<T> {

    boolean matchType(Class<?> type);

    T convert(String argument);
}
