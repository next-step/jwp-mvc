package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class PathVariableTest {

    @ParameterizedTest
    @DisplayName("PathVariable 값을 가져 온다.")
    @ValueSource(strings = {"user, user2"})
    void getPathvariable(String userId) {
        // given
        final ArgumentResolver argumentResolver = argumentResolver(userId);

        // when
        final Object[] arguments = argumentResolver.resolve();

        // then
        assertThat(arguments[0]).isEqualTo(userId);
    }

    private ArgumentResolver argumentResolver(String userId) {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users/" + userId);
        return new PathVariableArgumentResolver(request, pathvariableMethod());
    }

    private Method pathvariableMethod() {
        return Arrays.stream(TestUserController.class.getDeclaredMethods())
                .filter(method -> Objects.equals(method.getName(), "show_pathvariable"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("찾으시는 Method가 존재하지 않습니다."));
    }
}
