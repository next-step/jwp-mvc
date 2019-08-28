package core.handler.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class StringConverters {

    Map<Class<?>, StringConverter> stringConverters = new HashMap<>();

    private StringConverters() {
        setDefaultConverter();
    }

    public static StringConverters getInstance() {
        return Lazy.INSTANCE;
    }

    public void addConverter(Class<?> convertType, StringConverter stringConverter) {
        this.stringConverters.put(convertType, stringConverter);
    }

    public boolean supports(Class<?> convertType) {
        return this.stringConverters.containsKey(convertType);
    }

    public StringConverter getConverter(Class<?> convertType) {
        StringConverter converter = this.stringConverters.get(convertType);
        return converter;
    }

    private void setDefaultConverter() {
        addConverter(int.class, Integer::parseInt);
        addConverter(Integer.class, Integer::parseInt);
        addConverter(long.class, Long::parseLong);
        addConverter(Long.class, Long::parseLong);
        addConverter(float.class, Float::parseFloat);
        addConverter(Float.class, Float::parseFloat);
        addConverter(short.class, Short::parseShort);
        addConverter(Short.class, Short::parseShort);
        addConverter(double.class, Double::parseDouble);
        addConverter(Double.class, Double::parseDouble);
        addConverter(char.class, (value) -> value.charAt(0));
        addConverter(Character.class, (value) -> value.charAt(0));
        addConverter(byte.class, Byte::valueOf);
        addConverter(Byte.class, Byte::valueOf);
        addConverter(BigInteger.class, BigInteger::new);
        addConverter(BigDecimal.class, BigDecimal::new);
        addConverter(String.class, (v) -> v);
    }

    private static class Lazy {
        private static StringConverters INSTANCE = new StringConverters();
    }


}
