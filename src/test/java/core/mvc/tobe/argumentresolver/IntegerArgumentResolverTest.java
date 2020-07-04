package core.mvc.tobe.argumentresolver;

import core.mvc.tobe.TestUserController;
import core.mvc.tobe.argumentresolver.custom.IntegerArgumentResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegerArgumentResolverTest {
    @DisplayName("Integer Type(int, long)만 지원한다.")
    @Test
    void support() {
        //given
        Class clazz = TestUserController.class;
        Method method = getMethod("create_int_long", clazz.getDeclaredMethods());
        int length = method.getParameters().length;
        IntegerArgumentResolver integerArgumentResolver = new IntegerArgumentResolver();

        //when, then
        for (int i = 0; i < length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i);
            assertThat(integerArgumentResolver.support(methodParameter)).isTrue();
        }
    }

    @DisplayName("Integer Type(int, long)만 지원한다.")
    @ParameterizedTest
    @ValueSource(strings = {"create_string", "create_javabean"})
    void notSupportWhenNotIntegerType(String methodName) {
        //given
        Class clazz = TestUserController.class;
        Method method = getMethod(methodName, clazz.getDeclaredMethods());
        int length = method.getParameters().length;
        IntegerArgumentResolver integerArgumentResolver = new IntegerArgumentResolver();

        //when, then
        for (int i = 0; i < length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i);
            assertThat(integerArgumentResolver.support(methodParameter)).isFalse();
        }
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
