package core.mvc.tobe.argumentresolver;

import core.mvc.tobe.TestUserController;
import core.mvc.tobe.argumentresolver.custom.PathVariableArgumentResolver;
import core.mvc.tobe.argumentresolver.custom.StringArgumentResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class PathVariableArgumentResolverTest {
    @DisplayName("@PathVariable 어노테이션이 있으면, 지원한다.")
    @Test
    void support() {
        //given
        Class clazz = TestUserController.class;
        Method method = getMethod("show_pathvariable", clazz.getDeclaredMethods());
        int length = method.getParameters().length;
        PathVariableArgumentResolver pathVariableArgumentResolver = new PathVariableArgumentResolver();

        //when, then
        for (int i = 0; i < length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i);
            assertThat(pathVariableArgumentResolver.support(methodParameter)).isTrue();
        }
    }

    @DisplayName("@PathVariable 어노테이션이 없으면, 지원하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"create_int_long", "create_string"})
    void notSupportWithoutAnnotation(String methodName) {
        //given
        Class clazz = TestUserController.class;
        Method method = getMethod(methodName, clazz.getDeclaredMethods());
        int length = method.getParameters().length;
        PathVariableArgumentResolver pathVariableArgumentResolver = new PathVariableArgumentResolver();

        //when, then
        for (int i = 0; i < length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i);
            assertThat(pathVariableArgumentResolver.support(methodParameter)).isFalse();
        }
    }




    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
