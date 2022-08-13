package core.mvc.tobe.handler.resolver;

import core.mvc.tobe.TestUser;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class HandlerMethodArgumentResolversTest {
    private static final HttpServletRequest REQUEST = new MockHttpServletRequest();
    private static final HttpServletResponse RESPONSE = new MockHttpServletResponse();

    private final HandlerMethodArgumentResolvers handlerMethodArgumentResolvers = new HandlerMethodArgumentResolvers();

    @DisplayName("메서드의 인자가 HttpServletRequest 또는 HttpServletResponse로 이뤄진 경우, 인자들을 순서대로 반환한다.")
    @ParameterizedTest
    @MethodSource("provideForResolveParameters")
    void resolveParameters_if_parameter_contains_request_or_response(String methodName, List<Object> expected) {
        Method methodWithRequestOrResponse = findByClassAndMethodName(methodName, Tester.class);

        Object[] actual = handlerMethodArgumentResolvers.resolveParameters(methodWithRequestOrResponse, REQUEST, RESPONSE);

        assertThat(List.of(actual))
                .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForResolveParameters() {
        return Stream.of(
                arguments("allArgumentMethod", List.of(REQUEST, RESPONSE)),
                arguments("responseArgumentMethod", List.of(RESPONSE)),
                arguments("requestArgumentMethod", List.of(REQUEST))
        );
    }

    @DisplayName("메서드의 인자가 없는 경우, 빈 배열을 반환한다.")
    @Test
    void resolveParameters_if_empty_parameters() {
        HandlerMethodArgumentResolvers handlerMethodArgumentResolvers = new HandlerMethodArgumentResolvers();
        Method methodWithRequestOrResponse = findByClassAndMethodName("noArgumentMethod", Tester.class);

        Object[] actual = handlerMethodArgumentResolvers.resolveParameters(methodWithRequestOrResponse, REQUEST, RESPONSE);

        assertThat(List.of(actual))
                .isEmpty();
    }

    @DisplayName("메서드의 인자가 데이터 바인딩을 지원하지 않는 타입인 경우, 예외발생")
    @Test
    void resolveParameters_exception() {
        HandlerMethodArgumentResolvers handlerMethodArgumentResolvers = new HandlerMethodArgumentResolvers();
        Method methodWithRequestOrResponse = findByClassAndMethodName("cannotResolveMethod", Tester.class);

        assertThatThrownBy(() -> handlerMethodArgumentResolvers.resolveParameters(methodWithRequestOrResponse, REQUEST, RESPONSE))
                .isInstanceOf(NoExistsArgumentResolverException.class)
                .hasMessage("Controller 실행에 필요한 매개변수에 값을 할당할 argumentResolver가 존재하지 않습니다.");
    }

    @DisplayName("메서드의 인자가 requestParameter에 동일한 이름으로 저장된 문자열인 경우 해당 requestParameter 값을 바인딩한다.")
    @Test
    void resolveParameters_string_value() {
        MockHttpServletRequest requestWithParameter = new MockHttpServletRequest();
        requestWithParameter.addParameter("name", "jordy");

        HandlerMethodArgumentResolvers handlerMethodArgumentResolvers = new HandlerMethodArgumentResolvers();
        Method methodWithRequestOrResponse = findByClassAndMethodName("stringArgumentMethod", Tester.class);

        Object[] actual = handlerMethodArgumentResolvers.resolveParameters(methodWithRequestOrResponse, requestWithParameter, RESPONSE);
        assertThat(actual).containsExactly("jordy");
    }

    @DisplayName("메서드의 인자가 requestParameter에 동일한 이름으로 저장된 int또는 long형인 경우 해당 requestParameter 값을 바인딩한다.")
    @Test
    void resolveParameters_int_long_value() {
        MockHttpServletRequest requestWithParameter = new MockHttpServletRequest();
        requestWithParameter.addParameter("id", "3000000000");
        requestWithParameter.addParameter("age", "50");

        HandlerMethodArgumentResolvers handlerMethodArgumentResolvers = new HandlerMethodArgumentResolvers();
        Method methodWithRequestOrResponse = findByClassAndMethodName("create_int_long", Tester.class);

        Object[] actual = handlerMethodArgumentResolvers.resolveParameters(methodWithRequestOrResponse, requestWithParameter, RESPONSE);
        assertThat(actual).containsExactly(3000000000L, 50);
    }

    private Method findByClassAndMethodName(String noArgumentMethod, Class<?> clazz) {
        Method methodWithRequestOrResponse = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> noArgumentMethod.equals(method.getName()))
                .findFirst().get();
        return methodWithRequestOrResponse;
    }

    private static class Tester {
        public void noArgumentMethod() {

        }

        public void allArgumentMethod(HttpServletRequest request, HttpServletResponse response) {

        }

        public void responseArgumentMethod(HttpServletResponse response) {

        }

        public void requestArgumentMethod(HttpServletRequest request) {

        }

        public void stringArgumentMethod(String name) {

        }

        public void cannotResolveMethod(TestUser testUser) {

        }

        public void create_int_long(Long id, int age) {}
    }
}