package core.mvc.tobe.resolver;

import core.mvc.tobe.TestUserController;
import core.mvc.tobe.resolver.method.MethodParameter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class MethodParameterTest {

    @Test
    @DisplayName("메서드 파라미터 테스트")
    void getParameterNameTest() throws NoSuchMethodException {
        Class<TestUserController> testUserControllerClass = TestUserController.class;
        Method method = testUserControllerClass.getDeclaredMethod("create_string", String.class, String.class);

        MethodParameter firstParameter = new MethodParameter(method, 0);
        MethodParameter secondParameter = new MethodParameter(method, 1);

        Assertions.assertThat(firstParameter.getMethodParameterName()).isEqualTo("userId");
        Assertions.assertThat(secondParameter.getMethodParameterName()).isEqualTo("password");

    }
}
