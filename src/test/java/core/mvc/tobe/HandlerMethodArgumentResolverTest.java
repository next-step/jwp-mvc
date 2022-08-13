package core.mvc.tobe;

import core.mvc.tobe.handler.mapping.HandlerExecution;
import core.mvc.tobe.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerMethodArgumentResolverTest {
    private static final Class<?> CLAZZ = TestUserController.class;

    private HandlerExecution handlerExecution;

    @DisplayName("문자열 인자에 requestParameter 값을 할당받은 Controller 메소드를 실행한다.")
    @Test
    void create_string() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String userId = "javajigi";
        String password = "password";
        request.addParameter("userId", userId);
        request.addParameter("password", password);

        Object invoker = CLAZZ.getDeclaredConstructor().newInstance();
        String testMethodName = "create_string";
        Method method = getMethod(testMethodName, CLAZZ.getDeclaredMethods());
        handlerExecution = new HandlerExecution(invoker, method);

        // when
        ModelAndView result = handlerExecution.handle(request, new MockHttpServletResponse());

        // then
        assertThat(result.getObject("userId")).isEqualTo(userId);
        assertThat(result.getObject("password")).isEqualTo(password);
    }

    @DisplayName("숫자형 인자에 requestParameter 값을 할당받은 Controller 메소드를 실행한다.")
    @Test
    void create_int_long() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", "3000000000");
        request.addParameter("age", "20");

        Object invoker = CLAZZ.getDeclaredConstructor().newInstance();
        String testMethodName = "create_int_long";
        Method method = getMethod(testMethodName, CLAZZ.getDeclaredMethods());
        handlerExecution = new HandlerExecution(invoker, method);

        // when
        ModelAndView result = handlerExecution.handle(request, new MockHttpServletResponse());

        // then
        assertThat(result.getObject("id")).isEqualTo(3000000000L);
        assertThat(result.getObject("age")).isEqualTo(20);
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
