package core.mvc.tobe.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TypeConverterTest {
    @DisplayName("String input 에 대한 형변환을 수행한다.")
    @ParameterizedTest
    @MethodSource("supportType")
    void typeConvert(Class<?> type, String input, Object expectedOutput) {
        Object actualOutput = TypeConverter.convert(type, input);
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

    @DisplayName("형변환을 지원하지 않는 타입을 형변환 시 예외가 발생한다.")
    @Test
    void throwExceptionNotSupportType() {
        assertThatThrownBy(() -> TypeConverter.convert(Objects.class, "500")).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> supportType() { // argument source method
        return Stream.of(
                Arguments.of(int.class, "1", 1),
                Arguments.of(Integer.class, "1", 1),
                Arguments.of(long.class, "5000000000", 5000000000L),
                Arguments.of(Long.class, "5000000000", 5000000000L),
                Arguments.of(boolean.class, "true", true),
                Arguments.of(Boolean.class, "true", true),
                Arguments.of(double.class, "1.0", 1.0),
                Arguments.of(Double.class, "1.0", 1.0),
                Arguments.of(String.class, "test", "test")
        );
    }
}
