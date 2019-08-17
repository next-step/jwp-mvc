package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringTypeConverterTest {

    private StringTypeConverter stringTypeConverter;

    @BeforeEach
    void setup() {
        this.stringTypeConverter = new StringTypeConverter();
    }

    @Test
    void support() throws Exception {
        assertThat(stringTypeConverter.supports(String.class)).isTrue();
        assertThat(stringTypeConverter.supports(Object.class)).isFalse();
    }

    @Test
    void convert() throws Exception {
        String value = "ss111";

        assertThat(stringTypeConverter.convert(String.class, value)).isEqualTo(value);
    }

    @Test
    void convert_NullValue() throws Exception {
        String value = null;

        assertThat(stringTypeConverter.convert(String.class, value)).isEqualTo(value);
    }

    @Test
    void convert_BlankValue() throws Exception {
        String value = " ";

        assertThat(stringTypeConverter.convert(String.class, value)).isEqualTo(value);
    }
}