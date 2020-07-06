package core.mvc.tobe;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MethodParametersTest {

    @DisplayName("TestUserController의 create_string 메소드 파라미터 추출")
    @Test
    void test_extractMethodParameters_stringType() {
        Class<TestUserController> clazz = TestUserController.class;
        Method method = getMethod("create_string", clazz.getDeclaredMethods());

        MethodParameters methodParameters = MethodParameters.from(method);

        assertThat(methodParameters.getParameters())
                .extracting("getName", "getType")
                .containsExactlyInAnyOrder(Tuple.tuple("userId", String.class), Tuple.tuple("password", String.class));
    }

    @DisplayName("TestUserController의 create_int_long 메소드 파라미터 추출")
    @Test
    void test_extractMethodParameters_primitiveType() {
        Class<TestUserController> clazz = TestUserController.class;
        Method method = getMethod("create_int_long", clazz.getDeclaredMethods());

        MethodParameters methodParameters = MethodParameters.from(method);

        assertThat(methodParameters.getParameters())
                .extracting("getName", "getType")
                .containsExactlyInAnyOrder(Tuple.tuple("id", long.class), Tuple.tuple("age", int.class));
    }

    private Method getMethod(String methodName, Method[] declaredMethods) {
        return Arrays.stream(declaredMethods)
                .filter(method -> method.getName().equals(methodName))
                .findFirst()
                .get();
    }
}
