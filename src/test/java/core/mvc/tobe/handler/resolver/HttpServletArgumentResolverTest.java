package core.mvc.tobe.handler.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class HttpServletArgumentResolverTest {
    private static final NamedParameter REQUEST_TYPE_PARAMETER;
    private static final NamedParameter RESPONSE_TYPE_PARAMETER;
    private static final NamedParameter OTHER_TYPE_PARAMETER;

    private static final HttpServletRequest REQUEST = new MockHttpServletRequest();
    private static final HttpServletResponse RESPONSE = new MockHttpServletResponse();

    private final HttpServletArgumentResolver httpServletArgumentResolver = new HttpServletArgumentResolver();

    static {
        Method testClassMethod = TestClass.class.getDeclaredMethods()[0];
        Parameter[] parameters = testClassMethod.getParameters();

        REQUEST_TYPE_PARAMETER = new NamedParameter(parameters[0], "request");
        RESPONSE_TYPE_PARAMETER = new NamedParameter(parameters[1], "response");
        OTHER_TYPE_PARAMETER = new NamedParameter(parameters[2], "other");
    }

    @DisplayName("파라미터의 인자가 HttpServletRequest 또는 HttpServletResponse인 경우 true반환")
    @ParameterizedTest
    @MethodSource("provideForSupport")
    void support(NamedParameter namedParameter, boolean expected) {
        boolean actual = httpServletArgumentResolver.support(namedParameter);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideForSupport() {
        return Stream.of(
                arguments(REQUEST_TYPE_PARAMETER, true),
                arguments(RESPONSE_TYPE_PARAMETER, true),
                arguments(OTHER_TYPE_PARAMETER, false)
        );
    }

    @DisplayName("파라미터의 타입이에 따라 request 또는 response 객체를 그대로 반환한다.")
    @ParameterizedTest
    @MethodSource("provideForResolve")
    void resolve(NamedParameter namedParameter, Object expected) {
        Object actual = httpServletArgumentResolver.resolve(namedParameter, REQUEST, RESPONSE);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideForResolve() {
        return Stream.of(
                arguments(REQUEST_TYPE_PARAMETER, REQUEST),
                arguments(RESPONSE_TYPE_PARAMETER, RESPONSE)
        );
    }

    @DisplayName("지원하지 않는 resolving 타입의 경우 예외 발생 ")
    @Test
    void resolve_fail() {
        assertThatThrownBy(() -> httpServletArgumentResolver.resolve(OTHER_TYPE_PARAMETER, REQUEST, RESPONSE))
                .isInstanceOf(ArgumentResolveFailException.class)
                .hasMessage("Request / Response 타입 외에 인자는 바인딩 할 수 없습니다.");
    }

    private static class TestClass {
        public void test(HttpServletRequest request, HttpServletResponse response, Integer other) {

        }
    }
}