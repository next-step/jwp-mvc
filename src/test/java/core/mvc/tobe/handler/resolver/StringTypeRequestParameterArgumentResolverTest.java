package core.mvc.tobe.handler.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

class StringTypeRequestParameterArgumentResolverTest {
    private static final NamedParameter STRING_TYPE_PARAMETER;
    private static final NamedParameter OTHER_TYPE_PARAMETER;

    private final StringTypeRequestParameterArgumentResolver argumentResolver = new StringTypeRequestParameterArgumentResolver();

    static {
        Method testClassMethod = TestClass.class.getDeclaredMethods()[0];
        Parameter[] parameters = testClassMethod.getParameters();

        STRING_TYPE_PARAMETER = new NamedParameter(parameters[0], "name");
        OTHER_TYPE_PARAMETER = new NamedParameter(parameters[1], "other");
    }

    @DisplayName("파라미터의 인자가 문자열인 경우 true반환")
    @ParameterizedTest
    @MethodSource("provideForSupport")
    void support(NamedParameter namedParameter, boolean expected) {
        boolean actual = argumentResolver.support(namedParameter);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideForSupport() {
        return Stream.of(
                arguments(STRING_TYPE_PARAMETER, true),
                arguments(OTHER_TYPE_PARAMETER, false)
        );
    }

    @DisplayName("파라미터명과 일치하는 requestParameter가 값을 가지고 있는 경우 해당 값을 반환")
    @Test
    void resolve() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("name", "jordy");

        Object actual = argumentResolver.resolve(STRING_TYPE_PARAMETER, request, new MockHttpServletResponse());

        assertThat(actual).isEqualTo("jordy");
    }


    @DisplayName("파라미터명과 일치하는 requestParameter가 값을 가지지 않는 경우 예외 발생")
    @Test
    void resolve_fail() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThatThrownBy(() -> argumentResolver.resolve(STRING_TYPE_PARAMETER, request, new MockHttpServletResponse()))
                .isInstanceOf(ArgumentResolveFailException.class)
                .hasMessage("requestParameter - [name] 값이 null입니다.");
    }

    private static class TestClass {
        public void test(String name, int other) {

        }
    }
}