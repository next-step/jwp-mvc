package core.mvc.argumentresolver.converter;

public class IntegerParameterTypeConverter implements ParameterTypeConverter<Integer> {

    @Override
    public boolean matchType(Class<?> type) {
        return Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type);
    }

    @Override
    public Integer convert(String argument) {
        return Integer.parseInt(argument);
    }
}
