package core.mvc.tobe.argumentresolver;

import core.mvc.tobe.TestUserController;
import core.mvc.tobe.argumentresolver.custom.StringArgumentResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StringArgumentResolverTest {
    @DisplayName("String 타입의 파라미터는 지원(support)한다.")
    @Test
    void support() {
        //given
        Class clazz = TestUserController.class;
        Method method = getMethod("create_string", clazz.getDeclaredMethods());
        int length = method.getParameters().length;
        StringArgumentResolver stringArgumentResolver = new StringArgumentResolver();

        //when, then
        for (int i = 0; i < length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i);
            assertThat(stringArgumentResolver.support(methodParameter)).isTrue();
        }
    }

    @DisplayName("String 타입이 아닌 파라미터는 지원하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"create_int_long", "create_javabean"})
    void notSupportWhenNotString(String methodName) {
        //given
        Class clazz = TestUserController.class;
        Method method = getMethod(methodName, clazz.getDeclaredMethods());
        int length = method.getParameters().length;
        StringArgumentResolver stringArgumentResolver = new StringArgumentResolver();

        //when, then
        for (int i = 0; i < length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i);
            assertThat(stringArgumentResolver.support(methodParameter)).isFalse();
        }
    }

    @DisplayName("String 파라미터가 들어오면, 해당 Parameter를 결정해서 Object로 반환한다")
    @Test
    void resolve() {
        //given
        Class clazz = TestUserController.class;
        Method method = getMethod("create_string", clazz.getDeclaredMethods());
        int length = method.getParameters().length;
        StringArgumentResolver stringArgumentResolver = new StringArgumentResolver();

        //when
        List<Object> parameters = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i);
            Object parameter = stringArgumentResolver.resolve(methodParameter, createRequest(), new MockHttpServletResponse());
            parameters.add(parameter);
        }

        //then
        assertThat(parameters).hasSize(2);
        assertThat(parameters.contains("javajigi")).isTrue();
        assertThat(parameters.contains("password")).isTrue();
    }

    private MockHttpServletRequest createRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "javajigi");
        request.addParameter("password", "password");
        return request;
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
