package core.mvc.tobe.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.annotation.web.PathVariable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class MethodParameterTest extends AbstractMethodArgumentResolverTest {

    @DisplayName("메소드와 파라미터 정보를 가진다")
    @Test
    void constructor() {
        final Method method = getMethodOfTestUserController("create_string");
        final Parameter parameter = method.getParameters()[0];
        final String parameterName = getParameterNames(method)[0];

        final MethodParameter actual = new MethodParameter(method, parameter, parameterName);

        assertThat(actual).isEqualTo(new MethodParameter(method, parameter, parameterName));
    }

    @DisplayName("모든 인자는 null이거나 비어 있을 수 없다")
    @ParameterizedTest
    @MethodSource
    void required_parameters(final Method method, final Parameter parameter, final String parameterName) {
        assertThatThrownBy(() -> new MethodParameter(method, parameter, parameterName))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> required_parameters() {
        final Method method = getMethodOfTestUserController("create_string");
        final Parameter parameter = method.getParameters()[0];
        final String parameterName = getParameterNames(method)[0];

        return Stream.of(
            Arguments.of(null, parameter, parameterName),
            Arguments.of(method, null, parameterName),
            Arguments.of(method, parameter, null),
            Arguments.of(method, parameter, ""),
            Arguments.of(method, parameter, " ")
        );
    }

    @DisplayName("파라미터의 타입을 반환한다")
    @Test
    void parameter_type() {
        final Method method = getMethodOfTestUserController("create_string");
        final Parameter parameter = method.getParameters()[0];
        final String parameterName = getParameterNames(method)[0];

        final MethodParameter methodParameter = new MethodParameter(method, parameter, parameterName);

        final Class<?> actual = methodParameter.getParameterType();

        assertThat(actual).isEqualTo(String.class);
    }

    @DisplayName("파라미터에 애너테이션이 적용되어 있는지 확인할 수 있다")
    @ParameterizedTest
    @CsvSource(value = {
        "create_string, false",
        "show_pathvariable, true"
    })
    void annotation_present(final String methodName, final boolean expected) {
        final Method method = getMethodOfTestUserController(methodName);
        final Parameter parameter = method.getParameters()[0];
        final String parameterName = getParameterNames(method)[0];

        final MethodParameter methodParameter = new MethodParameter(method, parameter, parameterName);

        final boolean actual = methodParameter.hasParameterAnnotationPresent(PathVariable.class);

        assertThat(actual).isEqualTo(expected);
    }
}
