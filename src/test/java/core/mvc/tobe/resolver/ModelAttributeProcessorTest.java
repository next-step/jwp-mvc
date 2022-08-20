package core.mvc.tobe.resolver;

import core.mvc.tobe.TestUser;
import core.mvc.tobe.TestUserController;
import core.mvc.tobe.resolver.method.MethodParameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaBeanArgumentResolverTest {

    @Test
    @DisplayName("javaBean 데이터 매핑")
    void java_bean_value() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.addParameter("userId", "user");
        request.addParameter("password", "password");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Class<TestUserController> testUserControllerClass = TestUserController.class;
        Method method = testUserControllerClass.getDeclaredMethod("create_javabean", TestUser.class);
//        ModelAttributeMethodProcessor modelArgumentResolver = new ModelAttributeMethodProcessor();
        MethodParameter methodParameter = new MethodParameter(method, 0);
//        boolean support = modelArgumentResolver.supportsParameter(methodParameter);
//        assertThat(support).isTrue();
//        modelArgumentResolver.resolveArgument(methodParameter, request, response);

//        Assertions.assertAll(
//                () -> assertThat(user.getUserId()).isEqualTo("user"),
//                () -> assertThat(user.getPassword()).isEqualTo("password"),
//                () -> assertThat(user.getAge()).isZero()
//        );

    }
}
