package core.mvc.tobe.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.tobe.TestUser;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PrimitiveParameterTest {

    @DisplayName("원시 타입과 일치하는 열거 타입을 반환한다")
    @ParameterizedTest
    @MethodSource
    void constructor(final Class<?> clazz, final PrimitiveParameter expected) {
        final PrimitiveParameter actual = PrimitiveParameter.from(clazz);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> constructor() {
        return Stream.of(
            Arguments.of(int.class, PrimitiveParameter.INTEGER),
            Arguments.of(Integer.class, PrimitiveParameter.INTEGER),
            Arguments.of(long.class, PrimitiveParameter.LONG),
            Arguments.of(Long.class, PrimitiveParameter.LONG),
            Arguments.of(String.class, PrimitiveParameter.STRING)
        );
    }

    @DisplayName("원시 타입별 기본 값을 반환한다")
    @ParameterizedTest
    @MethodSource
    void default_values_of_primitive_types(final Class<?> clazz, final Object expected) {
        final PrimitiveParameter primitiveParameter = PrimitiveParameter.from(clazz);

        final Object actual = primitiveParameter.getDefaultValue();

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> default_values_of_primitive_types() {
        return Stream.of(
            Arguments.of(int.class, 0),
            Arguments.of(Integer.class, 0),
            Arguments.of(long.class, 0L),
            Arguments.of(Long.class, 0L),
            Arguments.of(String.class, null)
        );
    }

    @DisplayName("주어진 값을 원시 타입으로 변환하여 반환한다")
    @ParameterizedTest
    @MethodSource
    void convert(final Class<?> clazz, final String value, final Class<?> expectedType, final Object expectedValue) {
        final PrimitiveParameter primitiveParameter = PrimitiveParameter.from(clazz);

        final Object actual = primitiveParameter.convert(value);

        org.junit.jupiter.api.Assertions.assertAll(
            () -> assertThat(actual).isInstanceOf(expectedType),
            () -> assertThat(actual).isEqualTo(expectedValue)
        );

    }

    private static Stream<Arguments> convert() {
        return Stream.of(
            Arguments.of(int.class, "100", Integer.class, 100),
            Arguments.of(Integer.class, "200", Integer.class, 200),
            Arguments.of(long.class, "100", Long.class, 100L),
            Arguments.of(Long.class, "200", Long.class, 200L),
            Arguments.of(String.class, "value", String.class, "value")
        );
    }

    @DisplayName("원시 타입 여부를 확인할 수 있다")
    @ParameterizedTest
    @MethodSource
    void is_primitive_type(final Class<?> clazz, final boolean expected) {
        final boolean actual = PrimitiveParameter.isPrimitive(clazz);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> is_primitive_type() {
        return Stream.of(
            Arguments.of(int.class, true),
            Arguments.of(Integer.class, true),
            Arguments.of(long.class, true),
            Arguments.of(Long.class, true),
            Arguments.of(String.class, true),
            Arguments.of(TestUser.class, false),
            Arguments.of(HttpServletRequest.class, false)
        );
    }
}
