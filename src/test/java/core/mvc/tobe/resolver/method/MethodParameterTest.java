package core.mvc.tobe.resolver.method;

import core.mvc.tobe.TestUserController;
import core.mvc.tobe.resolver.method.MethodParameter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

public class MethodParameterTest {

    @Test
    @DisplayName("메서드 파라미터 이름 매핑")
    void method_param_name_type() throws NoSuchMethodException {
        Class<TestUserController> testUserControllerClass = TestUserController.class;
        Method method = testUserControllerClass.getDeclaredMethod("create_string", String.class, String.class);

        MethodParameter firstParameter = new MethodParameter(method, 0);
        MethodParameter secondParameter = new MethodParameter(method, 1);

        Assertions.assertThat(firstParameter.getParameterName()).isEqualTo("userId");
        Assertions.assertThat(firstParameter.getParameterType()).isEqualTo(String.class);

        Assertions.assertThat(secondParameter.getParameterName()).isEqualTo("password");
        Assertions.assertThat(secondParameter.getParameterType()).isEqualTo(String.class);
        Assertions.assertThat(firstParameter.hasAnnotationWithPathVariable()).isFalse();
    }
}
