package core.mvc.tobe.argumentresolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.mvc.tobe.TestUser;
import core.mvc.tobe.TestUserController;
import core.mvc.tobe.argumentresolver.custom.BeanArgumentResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanArgumentResolverTest {
    private static final TestUser TEST_USER = new TestUser("javajigi", "password", 20);

    @DisplayName("String 또는 Integer(int, long) 타입이 아닌 파라미터를 지원한다.")
    @Test
    void support() {
        //given
        Class clazz = TestUserController.class;
        Method method = getMethod("create_javabean", clazz.getDeclaredMethods());
        int length = method.getParameters().length;
        BeanArgumentResolver beanArgumentResolver = new BeanArgumentResolver();

        //when, then
        for (int i = 0; i < length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i);
            assertThat(beanArgumentResolver.support(methodParameter)).isTrue();
        }
    }

    @DisplayName("String 또는 Integer(int, long) 타입은 지원하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"create_int_long", "create_string"})
    void notSupportWhenStringOrInteger(String methodName) {
        //given
        Class clazz = TestUserController.class;
        Method method = getMethod(methodName, clazz.getDeclaredMethods());
        int length = method.getParameters().length;
        BeanArgumentResolver beanArgumentResolver = new BeanArgumentResolver();

        //when, then
        for (int i = 0; i < length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i);
            assertThat(beanArgumentResolver.support(methodParameter)).isFalse();
        }
    }

    @DisplayName("파라미터를 Object로 반환")
    @Test
    void resolve() throws JsonProcessingException, IllegalAccessException, InstantiationException, InvocationTargetException {
        //given
        Class clazz = TestUserController.class;
        Method method = getMethod("create_javabean", clazz.getDeclaredMethods());
        int length = method.getParameters().length;
        BeanArgumentResolver beanArgumentResolver = new BeanArgumentResolver();

        //when
        List<Object> parameters = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, i);
            Object parameter = beanArgumentResolver.resolve(methodParameter, createRequest(), new MockHttpServletResponse());
            parameters.add(parameter);
        }

        //then
        assertThat(parameters).hasSize(1);
        assertThat(parameters).contains(TEST_USER);
    }

    private MockHttpServletRequest createRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "javajigi");
        request.addParameter("password", "password");
        request.addParameter("age", "20");
        return request;
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
