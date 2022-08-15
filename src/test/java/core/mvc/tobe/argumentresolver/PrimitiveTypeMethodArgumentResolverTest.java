package core.mvc.tobe.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class PrimitiveTypeMethodArgumentResolverTest extends AbstractMethodArgumentResolverTest {

    private final MethodArgumentResolver resolver = new PrimitiveTypeMethodArgumentResolver();

    @DisplayName("원시 타입 파라미터가 아니면 변환할 수 없다")
    @Test
    void cannot_resolve_when_not_primitive_type() {
        final Method javaBeanMethod = getMethodOfTestUserController("create_javabean");
        final Method primitiveMethod = getMethodOfTestUserController("create_int_long");

        final Parameter[] parameters = javaBeanMethod.getParameters();
        final String[] parameterNames = getParameterNames(primitiveMethod);
        final MethodParameter methodParameter = new MethodParameter(primitiveMethod, parameters[0], parameterNames[0]);

        final boolean actual = resolver.resolvable(methodParameter);

        assertThat(actual).isFalse();
    }

    @DisplayName("원시 타입 파라미터는 변환할 수 있다")
    @Test
    void resolvable_when_primitive_type() {
        final Method primitiveMethod = getMethodOfTestUserController("create_int_long");
        final Parameter[] parameters = primitiveMethod.getParameters();
        final String[] parameterNames = getParameterNames(primitiveMethod);
        final MethodParameter methodParameter = new MethodParameter(primitiveMethod, parameters[0], parameterNames[0]);

        final boolean actual = resolver.resolvable(methodParameter);

        assertThat(actual).isTrue();
    }

    @DisplayName("원시 타입의 Wrapper 파라미터는 변환할 수 있다")
    @Test
    void resolvable_when_primitive_wrapper_type() {
        final Method primitiveWrapperMethod = getMethodOfTestUserController("create_wrapper_int_long");
        final Parameter[] parameters = primitiveWrapperMethod.getParameters();
        final String[] parameterNames = getParameterNames(primitiveWrapperMethod);
        final MethodParameter methodParameter = new MethodParameter(primitiveWrapperMethod, parameters[0], parameterNames[0]);

        final boolean actual = resolver.resolvable(methodParameter);

        assertThat(actual).isTrue();
    }

    @DisplayName("String 타입 파라미터는 변환할 수 있다")
    @Test
    void resolvable_when_string_type() {
        final Method stringMethod = getMethodOfTestUserController("create_string");
        final Parameter[] parameters = stringMethod.getParameters();
        final String[] parameterNames = getParameterNames(stringMethod);
        final MethodParameter methodParameter = new MethodParameter(stringMethod, parameters[0], parameterNames[0]);

        final boolean actual = resolver.resolvable(methodParameter);

        assertThat(actual).isTrue();
    }

    @DisplayName("원시타입 파라미터를 Object 타입으로 반한환다")
    @Test
    void returns_the_type_of_http_servlet_request() {
        final Method primitiveMethod = getMethodOfTestUserController("create_int_long");
        final Parameter[] parameters = primitiveMethod.getParameters();
        final String[] parameterNames = getParameterNames(primitiveMethod);

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", String.valueOf(Long.MAX_VALUE));
        request.addParameter("age", String.valueOf(Integer.MAX_VALUE));

        final Object[] actual = IntStream.range(0, parameters.length)
            .mapToObj(i -> resolver.resolve(new MethodParameter(primitiveMethod, parameters[i], parameterNames[i]), request))
            .toArray();

        assertAll(
            () -> assertThat(actual).hasSize(2),
            () -> assertThat(actual[0]).isEqualTo(Long.MAX_VALUE),
            () -> assertThat(actual[1]).isEqualTo(Integer.MAX_VALUE)
        );
    }

}
