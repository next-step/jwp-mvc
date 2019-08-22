package core.mvc;

import java.util.HashMap;
import java.util.Map;

public class StringConverters {

    Map<Class<?>, StringConverter> primitiveConverters = new HashMap<>();

    private StringConverters() {
        setDefaultConverter();
    }

    public static StringConverters getInstance() {
        return Lazy.INSTANCE;
    }

    public void addConverter(Class<?> convertType, StringConverter stringConverter) {
        this.primitiveConverters.put(convertType, stringConverter);
    }

    public boolean supports(Class<?> convertType) {
        return this.primitiveConverters.containsKey(convertType);
    }

    public StringConverter getConverter(Class<?> convertType) {
        StringConverter converter = this.primitiveConverters.get(convertType);
        return converter;
    }

    private void setDefaultConverter() {
        addConverter(int.class, Integer::parseInt);
        addConverter(long.class, Long::parseLong);
        addConverter(float.class, Float::parseFloat);
        addConverter(short.class, Short::parseShort);
        addConverter(double.class, Double::parseDouble);
        addConverter(char.class, (value) -> value.charAt(0));
        addConverter(byte.class, Byte::valueOf);
        addConverter(String.class, (v) -> v);
    }

    private static class Lazy {
        private static StringConverters INSTANCE = new StringConverters();
    }


}
