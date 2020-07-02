package core.mvc.argumentresolver.converter;

public class LongParameterTypeConverter implements ParameterTypeConverter<Long> {

    @Override
    public boolean matchType(Class<?> type) {
        return Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type);
    }

    @Override
    public Long convert(String argument) {
        return Long.parseLong(argument);
    }
}
