package core.mvc.mapping;

import core.mvc.resolver.TypeConverter;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TypeConverterTest {

    @Test
    void convert_numeric() throws Exception {
        Class<?> type = Long.class;

        Optional<?> optionalResult = TypeConverter.convert(type, "123");

        assertThat(optionalResult.isPresent()).isTrue();
        Object result = optionalResult.get();

        assertThat(result).isOfAnyClassIn(type);
        assertThat(result).isEqualTo(123L);
    }

    @Test
    void convert_string() throws Exception {
        Class<?> type = String.class;

        Optional<?> optionalResult = TypeConverter.convert(type, "123");

        assertThat(optionalResult.isPresent()).isTrue();
        Object result = optionalResult.get();

        assertThat(result).isOfAnyClassIn(type);
        assertThat(result).isEqualTo("123");
    }

    @Test
    void convert_Instance() {
        Class<?> type = TestClass.class;

        Optional<?> optionalResult = TypeConverter.convert(type, null);

        assertThat(optionalResult.isPresent()).isFalse();
    }

    private static class TestClass {

    }
}