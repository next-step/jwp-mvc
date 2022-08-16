package core.mvc.tobe.handler.resolver.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TypeUtilsTest {

    @ParameterizedTest
    @MethodSource("provideForIsSimpleType")
    void isSimpleType(Class<?> type, boolean expected) {
        assertThat(TypeUtils.isSimpleType(type)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideForIsSimpleType() {
        return Stream.of(
                arguments(int.class, true),
                arguments(long.class, true),
                arguments(short.class, true),
                arguments(byte.class, true),
                arguments(boolean.class, true),
                arguments(float.class, true),
                arguments(double.class, true),
                arguments(Integer.class, true),
                arguments(Long.class, true),
                arguments(Short.class, true),
                arguments(Byte.class, true),
                arguments(Boolean.class, true),
                arguments(Float.class, true),
                arguments(Double.class, true),
                arguments(String.class, true)
        );
    }
}