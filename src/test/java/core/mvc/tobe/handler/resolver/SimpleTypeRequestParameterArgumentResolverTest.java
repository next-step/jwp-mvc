package core.mvc.tobe.handler.resolver;

import core.mvc.tobe.TestUser;
import core.mvc.tobe.handler.resolver.utils.SimpleTypeConverter;
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
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SimpleTypeRequestParameterArgumentResolverTest {
    private static final NamedParameter INTEGER_TYPE_PARAMETER;
    private static final NamedParameter INT_TYPE_PARAMETER;
    private static final NamedParameter STRING_TYPE_PARAMETER;
    private static final NamedParameter OTHER_TYPE_PARAMETER;
    private static final NamedParameter WRAPPED_LONG_TYPE_PARAMETER;
    private static final NamedParameter LONG_TYPE_PARAMETER;

    private final SimpleTypeRequestParameterArgumentResolver argumentResolver = new SimpleTypeRequestParameterArgumentResolver(new SimpleTypeConverter());

    static {
        Method testClassMethod = TestClass.class.getDeclaredMethods()[0];
        Parameter[] parameters = testClassMethod.getParameters();

        INTEGER_TYPE_PARAMETER = new NamedParameter(parameters[0], "integer");
        INT_TYPE_PARAMETER = new NamedParameter(parameters[1], "intValue");
        STRING_TYPE_PARAMETER = new NamedParameter(parameters[2], "string");
        OTHER_TYPE_PARAMETER = new NamedParameter(parameters[3], "testUser");
        WRAPPED_LONG_TYPE_PARAMETER = new NamedParameter(parameters[4], "wrappedLong");
        LONG_TYPE_PARAMETER = new NamedParameter(parameters[5], "primitiveLong");

    }

    @DisplayName("primitive, 래핑된 primitive, 문자열 타입인 경우 true 반환")
    @ParameterizedTest
    @MethodSource("provideForSupport")
    void support(NamedParameter parameter, boolean expected) {
        assertThat(argumentResolver.support(parameter)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideForSupport() {
        return Stream.of(
                arguments(INTEGER_TYPE_PARAMETER, true),
                arguments(INT_TYPE_PARAMETER, true),
                arguments(STRING_TYPE_PARAMETER, true),
                arguments(OTHER_TYPE_PARAMETER, false),
                arguments(WRAPPED_LONG_TYPE_PARAMETER, true),
                arguments(LONG_TYPE_PARAMETER, true)
        );
    }

    @DisplayName("requestParameter 값들을 생성자 매개변수에 할당한다.")
    @ParameterizedTest
    @MethodSource("provideForResolve")
    void resolve(NamedParameter parameter, String parameterName, String parameterValue, Object expected) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(parameterName, parameterValue);

        Object actual = argumentResolver.resolve(parameter, request, new MockHttpServletResponse());


        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideForResolve() {
        return Stream.of(
                arguments(STRING_TYPE_PARAMETER, "string", "문자열", "문자열"),
                arguments(INTEGER_TYPE_PARAMETER, "integer", "2", 2),
                arguments(INT_TYPE_PARAMETER, "intValue", "20", 20),
                arguments(WRAPPED_LONG_TYPE_PARAMETER, "wrappedLong", "10000", 10000L),
                arguments(LONG_TYPE_PARAMETER, "primitiveLong", "100000", 100000L)
        );
    }

    private static class TestClass {
        public void test(Integer integer,
                         int intValue,
                         String string,
                         TestUser testUser,
                         Long wrappedLong,
                         long primitiveLong) {
        }
    }
}
