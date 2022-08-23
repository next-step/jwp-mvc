package core.mvc.tobe.resolver;

import core.mvc.tobe.TestUser;
import core.mvc.tobe.TestUserController;
import core.mvc.tobe.resolver.method.MethodParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelAttributeProcessorTest {

    @Test
    @DisplayName("javaBean 데이터 매핑")
    void java_bean_value() throws Exception {
        MockHttpServletRequest request = HttpRequestHelper.helper();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Class<TestUserController> testUserControllerClass = TestUserController.class;
        Method method = testUserControllerClass.getDeclaredMethod("create_javabean", TestUser.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        BasicArgumentResolver basicArgumentResolver = new BasicArgumentResolver();
        ModelAttributeMethodProcessor modelArgumentResolver = new ModelAttributeMethodProcessor(basicArgumentResolver);
        boolean support = modelArgumentResolver.supportsParameter(methodParameter);
        assertThat(support).isTrue();
        TestUser user = (TestUser) modelArgumentResolver.resolveArgument(methodParameter, request, response);

        Assertions.assertAll(
                () -> assertThat(user.getUserId()).isEqualTo("javajigi"),
                () -> assertThat(user.getPassword()).isEqualTo("password"),
                () -> assertThat(user.getAge()).isEqualTo(45)
        );

    }
}
