package core.mvc.tobe.argumentresolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import core.mvc.tobe.TestUser;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class CommandObjectMethodArgumentResolverTest extends AbstractMethodArgumentResolverTest {

    private final MethodArgumentResolver resolver = new CommandObjectMethodArgumentResolver();

    @DisplayName("원시 타입이거나 HttpServletRequest 타입이면 변환할 수 없다")
    @Test
    void returns_false_when_not_of_command_object() {
        final Method javaBeanMethod = getMethodOfTestUserController("create_javabean");
        final Method pathVariableMethod = getMethodOfTestUserController("create_int_long");

        final Parameter[] parameters = pathVariableMethod.getParameters();
        final String[] parameterNames = getParameterNames(javaBeanMethod);
        final MethodParameter methodParameter = new MethodParameter(javaBeanMethod, parameters[0], parameterNames[0]);

        final boolean actual = resolver.resolvable(methodParameter);

        assertThat(actual).isFalse();
    }

    @DisplayName("원시 타입이거나 HttpServletRequest 타입이 아니면 변환할 수 있다")
    @Test
    void returns_true_when_not_of_command_object() {
        final Method javaBeanMethod = getMethodOfTestUserController("create_javabean");

        final Parameter[] parameters = javaBeanMethod.getParameters();
        final String[] parameterNames = getParameterNames(javaBeanMethod);
        final MethodParameter methodParameter = new MethodParameter(javaBeanMethod, parameters[0], parameterNames[0]);

        final boolean actual = resolver.resolvable(methodParameter);

        assertThat(actual).isTrue();
    }

    @DisplayName("CommandObject를 Object로 반환한다")
    @Test
    void returns_command_object_with_value() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "admin");
        request.addParameter("password", "pass");

        final Method javaBeanMethod = getMethodOfTestUserController("create_javabean");

        final Parameter[] parameters = javaBeanMethod.getParameters();
        final String[] parameterNames = getParameterNames(javaBeanMethod);
        final MethodParameter methodParameter = new MethodParameter(javaBeanMethod, parameters[0], parameterNames[0]);

        // when
        final TestUser actual = (TestUser) resolver.resolve(methodParameter, request);

        // then
        assertAll(
            () -> assertThat(actual.getUserId()).isEqualTo("admin"),
            () -> assertThat(actual.getPassword()).isEqualTo("pass"),
            () -> assertThat(actual.getAge()).isZero()
        );
    }
}
