package core.mvc.tobe.resolver;

import core.mvc.tobe.TestUser;
import core.mvc.tobe.TestUserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class PathVariableArgumentResolverTest {

    private final ArgumentResolver argumentResolver = new PathVariableArgumentResolver();

    @Test
    @DisplayName("@PathVariable 어노테이션이 없는 경우엔 실패")
    void fail() throws NoSuchMethodException {
        Method method = TestUserController.class.getDeclaredMethod("create_javabean", TestUser.class);
        HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

        assertThat(argumentResolver.supportsParameter(methodParameters[0])).isFalse();
    }

    @Test
    @DisplayName("@PathVariable 어노테이션이 있는 경우엔 성공")
    void success() throws NoSuchMethodException {
        Method method = TestUserController.class.getDeclaredMethod("show_pathvariable", long.class);
        HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

        assertThat(argumentResolver.supportsParameter(methodParameters[0])).isTrue();
    }

    @Test
    @DisplayName("Argument 변환 테스트")
    public void resolveStringTypeArgument() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/users/1");

        Method method = TestUserController.class.getDeclaredMethod("show_pathvariable", long.class);
        HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

        assertThat(argumentResolver.resolveArgument(methodParameters[0], request, response)).isEqualTo(1L);
    }
}
