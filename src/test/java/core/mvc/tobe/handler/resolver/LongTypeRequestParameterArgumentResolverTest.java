package core.mvc.tobe.handler.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class LongTypeRequestParameterArgumentResolverTest {
    private static final NamedParameter WRAPPED_LONG_TYPE_PARAMETER;
    private static final NamedParameter PRIMITIVE_LONG_TYPE_PARAMETER;
    private static final NamedParameter OTHER_TYPE_PARAMETER;

    private final LongTypeRequestParameterArgumentResolver argumentResolver = new LongTypeRequestParameterArgumentResolver();

    static {
        Method testClassMethod = TestClass.class.getDeclaredMethods()[0];
        Parameter[] parameters = testClassMethod.getParameters();

        WRAPPED_LONG_TYPE_PARAMETER = new NamedParameter(parameters[0], "ageAsWrappedLong");
        PRIMITIVE_LONG_TYPE_PARAMETER = new NamedParameter(parameters[1], "ageAsPrimitiveLong");
        OTHER_TYPE_PARAMETER = new NamedParameter(parameters[2], "other");
    }

    @DisplayName("파라미터의 인자가 long/Long 타입인 경우 true반환")
    @ParameterizedTest
    @MethodSource("provideForSupport")
    void support(NamedParameter namedParameter, boolean expected) {
        boolean actual = argumentResolver.support(namedParameter);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideForSupport() {
        return Stream.of(
                arguments(WRAPPED_LONG_TYPE_PARAMETER, true),
                arguments(PRIMITIVE_LONG_TYPE_PARAMETER, true),
                arguments(OTHER_TYPE_PARAMETER, false)
        );
    }

    @DisplayName("파라미터명과 일치하는 requestParameter가 값을 가지고 있는 경우 해당 값을 숫자형으로 반환")
    @ParameterizedTest
    @MethodSource("provideForResolve")
    void resolve(String parameterName, NamedParameter parameter) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(parameterName, "20");

        Object actual = argumentResolver.resolve(parameter, request, new MockHttpServletResponse());

        assertThat(actual).isEqualTo(20L);
    }

    private static Stream<Arguments> provideForResolve() {
        return Stream.of(
                arguments("ageAsWrappedLong", WRAPPED_LONG_TYPE_PARAMETER),
                arguments("ageAsPrimitiveLong", PRIMITIVE_LONG_TYPE_PARAMETER)
        );
    }

    @DisplayName("파라미터명과 일치하는 requestParameter가 값을 가지지 않는 경우 예외 발생")
    @ParameterizedTest
    @MethodSource("provideForResolveFail")
    void resolve_fail(NamedParameter parameter, String parameterName) {
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThatThrownBy(() -> argumentResolver.resolve(parameter, request, new MockHttpServletResponse()))
                .isInstanceOf(ArgumentResolveFailException.class)
                .hasMessage("requestParameter - [" + parameterName +  "] 값이 null입니다.");
    }

    private static Stream<Arguments> provideForResolveFail() {
        return Stream.of(
                arguments(WRAPPED_LONG_TYPE_PARAMETER, "ageAsWrappedLong"),
                arguments(PRIMITIVE_LONG_TYPE_PARAMETER, "ageAsPrimitiveLong")
        );
    }

    private static class TestClass {
        public void test(Long ageAsWrappedLong, long ageAsPrimitiveLong,  String other) {

        }
    }
}