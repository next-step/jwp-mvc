package core.mvc.tobe.resolver;

import core.mvc.tobe.TestUserController;
import core.mvc.tobe.resolver.method.MethodParameter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;

public class BasicArgumentResolverTest {

    @Test
    @DisplayName("해당 String value가 잘 들어오는 확인 ")
    void resolver_value_confirm() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "javajigi");
        request.addParameter("password", "password");

        MockHttpServletResponse response = new MockHttpServletResponse();

        Class<TestUserController> testUserControllerClass = TestUserController.class;
        Method method = testUserControllerClass.getDeclaredMethod("create_string", String.class, String.class);

        MethodParameter firstParameter = new MethodParameter(method, 0);
        MethodParameter secondParameter = new MethodParameter(method, 1);

        BasicArgumentResolver resolver = new BasicArgumentResolver();
        Object value1 = resolver.resolveArgument(firstParameter, request, response);
        Object value2 = resolver.resolveArgument(secondParameter, request, response);
        Assertions.assertThat(value1).isEqualTo("javajigi");
        Assertions.assertThat(value2).isEqualTo("password");
    }

    @Test
    @DisplayName("해당 숫자 value가 잘 들어오는 확인 ")
    void resolver_value_confirm_number() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", "1");
        request.addParameter("age", "30");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Class<TestUserController> testUserControllerClass = TestUserController.class;
        Method method = testUserControllerClass.getDeclaredMethod("create_int_long", long.class, int.class);

        MethodParameter firstParameter = new MethodParameter(method, 0);
        MethodParameter secondParameter = new MethodParameter(method, 1);

        BasicArgumentResolver resolver = new BasicArgumentResolver();
        Object value1 = resolver.resolveArgument(firstParameter, request, response);
        Object value2 = resolver.resolveArgument(secondParameter, request, response);

        Assertions.assertThat(value1).isEqualTo(1L);
        Assertions.assertThat(value2).isEqualTo(30);
    }
}
