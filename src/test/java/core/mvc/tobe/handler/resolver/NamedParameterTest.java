package core.mvc.tobe.handler.resolver;

import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Parameter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class NamedParameterTest {

    private NamedParameter namedParameter;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        String methodName = "method";
        Parameter parameter = TestClass.class
                .getDeclaredMethod(methodName, Integer.TYPE)
                .getParameters()[0];
        namedParameter = new NamedParameter(parameter, methodName);
    }

    @DisplayName("파라미터의 class 타입을 반환한다.")
    @Test
    void getType() {
        Class<?> actual = namedParameter.getType();
        assertThat(actual).isEqualTo(Integer.TYPE);
    }

    @DisplayName("인자로 넘어온 타입과 파라미터 타입의 일치 여부를 반환한다.")
    @ParameterizedTest
    @MethodSource("providedForIsEqualType")
    void isEqualsType(Class<?> type, boolean expected) {
        boolean actual = namedParameter.isEqualsType(type);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> providedForIsEqualType() {
        return Stream.of(
                arguments(Integer.TYPE, true),
                arguments(Integer.class, false),
                arguments(Long.TYPE, false),
                arguments(Long.class, false),
                arguments(TestUser.class, false)
        );
    }

    private static class TestClass {
        public void method(int param) {

        }
    }
}