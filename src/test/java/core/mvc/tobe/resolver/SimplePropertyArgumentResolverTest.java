package core.mvc.tobe.resolver;

import core.mvc.tobe.TestUserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class SimplePropertyArgumentResolverTest {

    private ArgumentResolver argumentResolver = new SimplePropertyArgumentResolver();

    @Test
    @DisplayName("String Type 성공 테스트")
    void StringType_SupportsParameter() throws NoSuchMethodException {
        Method method = TestUserController.class.getDeclaredMethod("create_string", String.class, String.class);
        HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

        assertThat(argumentResolver.supportsParameter(methodParameters[0])).isTrue();
        assertThat(argumentResolver.supportsParameter(methodParameters[1])).isTrue();
    }

    @Test
    @DisplayName("primitive Type 성공 테스트")
    void primitiveType_supportsParameter2() throws NoSuchMethodException {
        Method method = TestUserController.class.getDeclaredMethod("create_int_long", long.class, int.class);
        HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

        assertThat(argumentResolver.supportsParameter(methodParameters[0])).isTrue();
        assertThat(argumentResolver.supportsParameter(methodParameters[1])).isTrue();
    }

    @Test
    @DisplayName("String Type 변환 테스트")
    void stringType_resolveArgument() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("userId", "tester");
        request.addParameter("password", "1234");

        Method method = TestUserController.class.getDeclaredMethod("create_string", String.class, String.class);
        HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

        assertThat(argumentResolver.resolveArgument(methodParameters[0], request, response)).isEqualTo("tester");
        assertThat(argumentResolver.resolveArgument(methodParameters[1], request, response)).isEqualTo("1234");
    }

    @Test
    @DisplayName("Primitive Type 변환 테스트")
    void primitiveType_resolveArgument() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("id", "1");
        request.addParameter("age", "111");

        Method method = TestUserController.class.getDeclaredMethod("create_int_long", long.class, int.class);
        HandlerMethod handlerMethod = new HandlerMethod(new TestUserController(), method);
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

        assertThat(argumentResolver.resolveArgument(methodParameters[0], request, response)).isEqualTo(1L);
        assertThat(argumentResolver.resolveArgument(methodParameters[1], request, response)).isEqualTo(111);
    }
}
