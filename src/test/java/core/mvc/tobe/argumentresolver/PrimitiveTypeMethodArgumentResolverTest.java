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


    @DisplayName("Parameter 가 없으면 변환할 수 없다")
    @Test
    void no_parameter() {
        final Method httpServletRequestMethod = getMethodOfTestUserController("create_int_long");

        final boolean actual = resolver.resolvable(httpServletRequestMethod, null);

        assertThat(actual).isFalse();
    }

    @DisplayName("원시 타입 파라미터가 아니면 변환할 수 없다")
    @Test
    void cannot_resolve_when_not_primitive_type() {
        final Method javaBeanMethod = getMethodOfTestUserController("create_javabean");
        final Method primitiveMethod = getMethodOfTestUserController("create_int_long");

        final boolean actual = resolver.resolvable(primitiveMethod, javaBeanMethod.getParameters()[0]);

        assertThat(actual).isFalse();
    }

    @DisplayName("원시 타입 파라미터는 변환할 수 있다")
    @Test
    void resolvable_when_primitive_type() {
        final Method primitiveMethod = getMethodOfTestUserController("create_int_long");

        final boolean actual = resolver.resolvable(primitiveMethod, primitiveMethod.getParameters()[0]);

        assertThat(actual).isTrue();
    }

    @DisplayName("원시 타입의 Wrapper 파라미터는 변환할 수 있다")
    @Test
    void resolvable_when_primitive_wrapper_type() {
        final Method primitiveWrapperMethod = getMethodOfTestUserController("create_wrapper_int_long");

        final boolean actual = resolver.resolvable(primitiveWrapperMethod, primitiveWrapperMethod.getParameters()[0]);

        assertThat(actual).isTrue();
    }

    @DisplayName("String 타입 파라미터는 변환할 수 있다")
    @Test
    void resolvable_when_string_type() {
        final Method stringMethod = getMethodOfTestUserController("create_string");

        final boolean actual = resolver.resolvable(stringMethod, stringMethod.getParameters()[0]);

        assertThat(actual).isTrue();
    }

    @DisplayName("원시타입 파라미터를 Object 타입으로 반한환다")
    @Test
    void returns_the_type_of_http_servlet_request() {
        final Method primitiveMethod = getMethodOfTestUserController("create_int_long");
        final String[] parameterNames = getParameterNames(primitiveMethod);
        final Parameter[] parameters = primitiveMethod.getParameters();

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", String.valueOf(Long.MAX_VALUE));
        request.addParameter("age", String.valueOf(Integer.MAX_VALUE));

        final Object[] actual = IntStream.range(0, parameters.length)
            .mapToObj(i -> resolver.resolve(primitiveMethod, parameters[i], parameterNames[i], request))
            .toArray();

        assertAll(
            () -> assertThat(actual).hasSize(2),
            () -> assertThat(actual[0]).isEqualTo(Long.MAX_VALUE),
            () -> assertThat(actual[1]).isEqualTo(Integer.MAX_VALUE)
        );
    }

}
