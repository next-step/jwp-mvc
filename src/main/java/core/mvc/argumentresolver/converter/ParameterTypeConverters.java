package core.mvc.argumentresolver.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParameterTypeConverters {

    private final List<ParameterTypeConverter<?>> converters = new ArrayList<>();

    public ParameterTypeConverters() {
        this.converters.addAll(Arrays.asList(
                new IntegerParameterTypeConverter(),
                new LongParameterTypeConverter()
        ));
    }

    public Object convert(String argument, Class<?> parameterType) {
        ParameterTypeConverter<?> typeConverter = this.converters.stream()
                .filter(converter -> converter.matchType(parameterType))
                .findFirst()
                .orElse(null);

        return (typeConverter == null) ? argument : typeConverter.convert(argument);
    }
}
