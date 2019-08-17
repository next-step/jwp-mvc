package core.mvc.mapping;

import core.mvc.resolver.NumericTypeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NumericTypeConverterTest {

    private NumericTypeConverter numericTypeConverter;

    @BeforeEach
    void setup() {
        this.numericTypeConverter = new NumericTypeConverter();
    }

    @Test
    void convert_int() throws Exception {
        int value = 1234;

        int result = numericTypeConverter.convert(int.class, String.valueOf(value));

        assertThat(result).isEqualTo(value);
    }

    @Test
    void convert_Integer() throws Exception {
        Integer value = 1234;

        Integer result = numericTypeConverter.convert(Integer.class, String.valueOf(value));

        assertThat(result).isEqualTo(value);
    }

    @Test
    void convert_long() throws Exception {
        long value = 1234;

        long result = numericTypeConverter.convert(long.class, String.valueOf(value));

        assertThat(result).isEqualTo(value);
    }

    @Test
    void convert_Long() throws Exception {
        Long value = 1234L;

        Long result = numericTypeConverter.convert(Long.class, String.valueOf(value));

        assertThat(result).isEqualTo(value);
    }

    @Test
    void convert_double() throws Exception {
        double value = 1234.1234;

        double result = numericTypeConverter.convert(double.class, String.valueOf(value));

        assertThat(result).isEqualTo(value);
    }

    @Test
    void convert_Double() throws Exception {
        Double value = 1234.1234;

        Double result = numericTypeConverter.convert(Double.class, String.valueOf(value));

        assertThat(result).isEqualTo(value);
    }

    @Test
    void convert_float() throws Exception {
        float value = 1234.1234f;

        float result = numericTypeConverter.convert(float.class, String.valueOf(value));

        assertThat(result).isEqualTo(value);
    }

    @Test
    void convert_Float() throws Exception {
        Float value = 1234.1234f;

        Float result = numericTypeConverter.convert(Float.class, String.valueOf(value));

        assertThat(result).isEqualTo(value);
    }

    @Test
    void convert_BlankValue() {
        String blankValue = " ";

        assertThatThrownBy(() -> numericTypeConverter.convert(Integer.class, blankValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("value 값이 비어있습니다.");
    }

}