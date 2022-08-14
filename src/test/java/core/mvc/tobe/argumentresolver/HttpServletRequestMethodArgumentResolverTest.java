package core.mvc.tobe.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class HttpServletRequestMethodArgumentResolverTest extends AbstractMethodArgumentResolverTest {

    private final MethodArgumentResolver resolver = new HttpServletRequestMethodArgumentResolver();

    @DisplayName("HttpServletRequest 타입이 아니면 변환할 수 없다")
    @Test
    void returns_false_when_not_type_of_http_servlet_request() {
        final Method httpServletRequestMethod = getMethodOfTestUserController("httpServletRequest");
        final Method pathVariableMethod = getMethodOfTestUserController("show_pathvariable");

        final Parameter[] parameters = httpServletRequestMethod.getParameters();
        final String[] parameterNames = getParameterNames(pathVariableMethod);
        final MethodParameter methodParameter = new MethodParameter(httpServletRequestMethod, parameters[0], parameterNames[0]);

        final boolean actual = resolver.resolvable(methodParameter);

        assertThat(actual).isFalse();
    }

    @DisplayName("HttpServletRequest 타입은 변환할 수 있다")
    @Test
    void resolvable_type_of_http_servlet_request() {
        final Method httpServletRequestMethod = getMethodOfTestUserController("httpServletRequest");
        final Parameter[] parameters = httpServletRequestMethod.getParameters();
        final String[] parameterNames = getParameterNames(httpServletRequestMethod);
        final MethodParameter methodParameter = new MethodParameter(httpServletRequestMethod, parameters[0], parameterNames[0]);

        final boolean actual = resolver.resolvable(methodParameter);

        assertThat(actual).isTrue();
    }

    @DisplayName("HttpServletRequest 타입은 변환하지 않고 반한환다")
    @Test
    void returns_the_type_of_http_servlet_request() {
        final Method httpServletRequestMethod = getMethodOfTestUserController("httpServletRequest");
        final Parameter[] parameters = httpServletRequestMethod.getParameters();
        final String[] parameterNames = getParameterNames(httpServletRequestMethod);
        final MethodParameter methodParameter = new MethodParameter(httpServletRequestMethod, parameters[0], parameterNames[0]);

        final MockHttpServletRequest request = new MockHttpServletRequest();

        final Object actual = resolver.resolve(methodParameter, request);

        assertAll(
            () -> assertThat(actual).isInstanceOf(HttpServletRequest.class),
            () -> assertThat(actual).isEqualTo(request)
        );
    }
}
