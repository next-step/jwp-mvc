package core.mvc.tobe.argumentresolver;

import core.mvc.tobe.TestUserController;
import core.mvc.tobe.argumentresolver.custom.PathVariableArgumentResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.InvocationTargetException;
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
            MethodParameter methodParameter = new MethodParameter(method, i, createRequest().getRequestURI());
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
            MethodParameter methodParameter = new MethodParameter(method, i, createRequest().getRequestURI());
            assertThat(pathVariableArgumentResolver.support(methodParameter)).isFalse();
        }
    }

    @DisplayName("@PathVariable")
    @Test
    void resolve() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //given
        Class clazz = TestUserController.class;
        Method method = getMethod("show_pathvariable", clazz.getDeclaredMethods());
        PathVariableArgumentResolver pathVariableArgumentResolver = new PathVariableArgumentResolver();
        MockHttpServletRequest request = createRequest();

        //when
        MethodParameter methodParameter = new MethodParameter(method, 0, request.getRequestURI());
        Object resolve = pathVariableArgumentResolver.resolve(methodParameter, request, new MockHttpServletResponse());

        //then
        assertThat(resolve).isEqualTo(1L);

    }

    private MockHttpServletRequest createRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users/" + Long.valueOf(1));
        return request;
    }


    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
