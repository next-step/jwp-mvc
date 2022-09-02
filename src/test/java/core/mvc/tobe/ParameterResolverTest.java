package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ParameterResolver 테스트")
class ParameterResolverTest {
    @DisplayName("PathVariable 인자를 구한다.")
    @Test
    void resolve() {
        // given
        String userId = "testUser";
        final ParameterResolver parameterResolver = getParameterResolver(userId);

        // when
        final Object[] parameters = parameterResolver.resolve();

        // then
        assertThat(parameters[0]).isEqualTo(userId);
    }

    private ParameterResolver getParameterResolver(String userId) {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users/" + userId);
        final Method method = getMethod(TestUserController.class, "show_pathvariable");
        return new ParameterResolver(request, new MockHttpServletResponse(), method);
    }

    private Method getMethod(Class<TestUserController> clazz, String methodName) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> Objects.equals(method.getName(), methodName))
                .findFirst()
                .get();
    }
}
