package core.mvc.resolver;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

public class TypeConverter {

    private static final List<ValueTypeConverter> VALUE_TYPE_CONVERTERS = ImmutableList.of(
         new StringTypeConverter(), new NumericTypeConverter()
    );

    public static <T> Optional<T> convert(Class<T> type, String value) {
        for (ValueTypeConverter valueTypeConverter : VALUE_TYPE_CONVERTERS) {
            if (valueTypeConverter.supports(type)) {
                return Optional.of(valueTypeConverter.convert(type, value));
            }
        }
        return Optional.empty();
    }

}
