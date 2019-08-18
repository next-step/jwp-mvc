package core.mvc.resolver;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class NumericTypeConverter implements ValueTypeConverter {

    private final Map<Class<?>, Function<String, ?>> numericConverters = new HashMap<>();

    public NumericTypeConverter() {
        init();
    }

    private void init() {
        numericConverters.put(int.class, Integer::valueOf);
        numericConverters.put(Integer.class, Integer::valueOf);
        numericConverters.put(long.class, Long::valueOf);
        numericConverters.put(Long.class, Long::valueOf);
        numericConverters.put(double.class, Double::valueOf);
        numericConverters.put(Double.class, Double::valueOf);
        numericConverters.put(float.class, Float::valueOf);
        numericConverters.put(Float.class, Float::valueOf);
    }

    @Override
    public boolean supports(Class<?> type) {
        return numericConverters.containsKey(type);
    }

    @Override
    public <T> T convert(Class<T> type, String value) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(value), "value 값이 비어있습니다.");

        if (!numericConverters.containsKey(type)) {
            throw new IllegalArgumentException("지원하지 않는 타입 입니다. type : [" + type + "]");
        }

        return (T) numericConverters.get(type).apply(value);
    }

}
