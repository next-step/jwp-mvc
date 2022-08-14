package core.mvc.tobe.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockHttpServletRequest;

class PathVariableMethodArgumentResolverTest extends AbstractMethodArgumentResolverTest {

    private final MethodArgumentResolver resolver = new PathVariableMethodArgumentResolver();

    @DisplayName("파라미터에 PathVariable 애너테이션이 없으면 변환할 수 없다")
    @ParameterizedTest
    @CsvSource(value = {
        "create_javabean, false",
        "show_pathvariable, true"
    })
    void cannot_resolvable_no_with_path_variable_annotation(final String methodName, final boolean expected) {
        final Method method = getMethodOfTestUserController(methodName);

        final Parameter[] parameters = method.getParameters();
        final String[] parameterNames = getParameterNames(method);
        final MethodParameter methodParameter = new MethodParameter(method, parameters[0], parameterNames[0]);

        final boolean actual = resolver.resolvable(methodParameter);

        assertThat(actual).isEqualTo(expected);

    }

    @DisplayName("PathVariable 애너테이션이 적용된 파라미터를 타입에 맞춰 Object 배열을 반환한다")
    @Test
    void returns_an_object_array_of_parameters_with_path_variable_types_of_method() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/100");

        final Method method = getMethodOfTestUserController("show_pathvariable");

        final Parameter[] parameters = method.getParameters();
        final String[] parameterNames = getParameterNames(method);
        final MethodParameter methodParameter = new MethodParameter(method, parameters[0], parameterNames[0]);

        // when
        final Object actual = resolver.resolve(methodParameter, request);

        // then
        assertAll(
            () -> assertThat(actual).isInstanceOf(Long.class),
            () -> assertThat(actual).isEqualTo(100L)
        );
    }
}
