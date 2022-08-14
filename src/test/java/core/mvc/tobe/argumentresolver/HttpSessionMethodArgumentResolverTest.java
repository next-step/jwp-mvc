package core.mvc.tobe.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class HttpSessionMethodArgumentResolverTest extends AbstractMethodArgumentResolverTest {

    private final MethodArgumentResolver resolver = new HttpSessionMethodArgumentResolver();

    @DisplayName("HttpSession 타입이 아니면 변환할 수 없다")
    @Test
    void returns_false_when_not_type_of_http_servlet_request() {
        final Method method = getMethodOfTestUserController("httpSession");
        final Method pathVariableMethod = getMethodOfTestUserController("show_pathvariable");

        final Parameter[] parameters = pathVariableMethod.getParameters();
        final String[] parameterNames = getParameterNames(method);
        final MethodParameter methodParameter = new MethodParameter(method, parameters[0], parameterNames[0]);

        final boolean actual = resolver.resolvable(methodParameter);

        assertThat(actual).isFalse();
    }

    @DisplayName("HttpSession 타입은 변환할 수 있다")
    @Test
    void resolvable_type_of_http_servlet_request() {
        final Method method = getMethodOfTestUserController("httpSession");
        final Parameter[] parameters = method.getParameters();
        final String[] parameterNames = getParameterNames(method);
        final MethodParameter methodParameter = new MethodParameter(method, parameters[0], parameterNames[0]);

        final boolean actual = resolver.resolvable(methodParameter);

        assertThat(actual).isTrue();
    }

    @DisplayName("HttpSession 타입은 변환하지 않고 반한환다")
    @Test
    void returns_the_type_of_http_servlet_request() {
        final Method method = getMethodOfTestUserController("httpSession");
        final Parameter[] parameters = method.getParameters();
        final String[] parameterNames = getParameterNames(method);
        final MethodParameter methodParameter = new MethodParameter(method, parameters[0], parameterNames[0]);

        final MockHttpServletRequest request = new MockHttpServletRequest();

        final Object actual = resolver.resolve(methodParameter, request);

        assertAll(
            () -> assertThat(actual).isInstanceOf(HttpSession.class),
            () -> assertThat(actual).isEqualTo(request.getSession())
        );
    }
}
