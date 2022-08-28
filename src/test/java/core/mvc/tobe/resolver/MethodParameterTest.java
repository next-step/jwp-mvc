package core.mvc.tobe.resolver;

import core.annotation.web.PathVariable;
import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
class MethodParameterTest extends MethodUtils {
    private static final List<MethodParameter> methodParameters = new ArrayList<>();

    @BeforeAll
    static void setUp() {
        Class clazz = MethodParameterTest.class;
        Method method = getMethod("testMethod", clazz.getDeclaredMethods());
        Parameter[] parameters = method.getParameters();
        int parameterCount = parameters.length;

        for (int idx = 0; idx < parameterCount; idx++) {
            methodParameters.add(new MethodParameter(method, parameters[idx], idx));
        }
    }

    @Test
    @DisplayName("메소드의 파라미터에 애노테이션이 존재하는지 확인한다.")
    void hasParameterAnnotation() {
        assertAll(
                () -> assertThat(methodParameters.get(0).hasParameterAnnotation(PathVariable.class)).isFalse(),
                () -> assertThat(methodParameters.get(1).hasParameterAnnotation(PathVariable.class)).isTrue(),
                () -> assertThat(methodParameters.get(2).hasParameterAnnotation(PathVariable.class)).isFalse()
        );
    }

    @Test
    @DisplayName("메소드의 파라미터의 타입과 일치하는지 확인한다.")
    void isParameterTypeEqual() {
        assertAll(
                () -> assertThat(methodParameters.get(0).isParameterTypeEqual(String.class)).isTrue(),
                () -> assertThat(methodParameters.get(1).isParameterTypeEqual(int.class)).isTrue(),
                () -> assertThat(methodParameters.get(2).isParameterTypeEqual(TestUser.class)).isTrue()
        );
    }

    @Test
    @DisplayName("메소드의 파라미터의 이름이 일치하는지 확인한다.")
    void getParameterName() {
        assertAll(
                () -> assertThat(methodParameters.get(0).getParameterName()).isEqualTo("name"),
                () -> assertThat(methodParameters.get(1).getParameterName()).isEqualTo("id"),
                () -> assertThat(methodParameters.get(2).getParameterName()).isEqualTo("testUser")
        );
    }

    @Test
    @DisplayName("primitive or wrapper type 인지 체크한다.")
    void isPrimitiveOrWrapperType() {
        assertAll(
                () -> assertThat(methodParameters.get(0).isPrimitiveOrWrapperType()).isTrue(),
                () -> assertThat(methodParameters.get(1).isPrimitiveOrWrapperType()).isTrue(),
                () -> assertThat(methodParameters.get(2).isPrimitiveOrWrapperType()).isFalse()
        );
    }

    @Test
    @DisplayName("model type 인지 체크한다.")
    void isModelType() {
        assertAll(
                () -> assertThat(methodParameters.get(0).isModelType()).isFalse(),
                () -> assertThat(methodParameters.get(1).isModelType()).isFalse(),
                () -> assertThat(methodParameters.get(2).isModelType()).isTrue()
        );
    }

    public void testMethod(String name, @PathVariable int id, TestUser testUser) {
    }
}
