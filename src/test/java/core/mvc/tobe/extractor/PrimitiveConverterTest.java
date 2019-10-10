package core.mvc.tobe.extractor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PrimitiveConverterTest {

    @DisplayName("지원하지 않는 타입 입력 시 입력 값을 반환한다.")
    @Test
    void convertNotSupportType() {
        final String value = "VALUE";

        assertThat(PrimitiveConverter.convert(Object.class, value)).isEqualTo(value);
        assertThat(PrimitiveConverter.convert(String.class, value)).isEqualTo(value);
        assertThat(PrimitiveConverter.convert(PrimitiveConverterTest.class, value)).isEqualTo(value);
    }

    @DisplayName("null 입력 시 null을 반환한다.")
    @Test
    void convertNull() {
        assertThat(PrimitiveConverter.convert(null, null)).isNull();
        assertThat(PrimitiveConverter.convert(String.class, null)).isNull();
        assertThat(PrimitiveConverter.convert(int.class, null)).isNull();
        assertThat(PrimitiveConverter.convert(Object.class, null)).isNull();
    }

    @DisplayName("integer 입력 시 integer 값을 반환한다.")
    @Test
    void convertInteger() {
        final int value = 100;

        assertThat(PrimitiveConverter.convert(Integer.class, String.valueOf(value))).isEqualTo(value);
    }

    @DisplayName("long 입력 시 long 값을 반환한다.")
    @Test
    void convertLong() {
        final long value = 100L;

        assertThat(PrimitiveConverter.convert(Long.class, String.valueOf(value))).isEqualTo(value);
    }
}