package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MethodParameterTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    void createTest() throws NoSuchMethodException {
        Class clazz = TestUserController.class;
        Method method = clazz.getMethod("create_string", String.class, String.class);

        MethodParameter methodParameter0 = new MethodParameter(method, 0);
        MethodParameter methodParameter1 = new MethodParameter(method, 1);

        assertThat(method.getName()).isEqualTo("create_string");
        assertThat(methodParameter0.getParameterName()).isEqualTo("userId");
        assertThat(methodParameter0.getParameterType()).isEqualTo(String.class);
        assertThat(methodParameter1.getParameterName()).isEqualTo("password");
        assertThat(methodParameter1.getParameterType()).isEqualTo(String.class);
    }

}
