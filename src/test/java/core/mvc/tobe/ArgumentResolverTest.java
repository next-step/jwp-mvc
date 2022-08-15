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

public class ArgumentResolverTest {
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

    @DisplayName("숫자형 인자에 requestParameter 값을 할당받은 Controller 메소드를 실행한다.")
    @Test
    void create_javabean() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "321");
        request.addParameter("password", "비밀번호");
        request.addParameter("age", "20");

        Object invoker = CLAZZ.getDeclaredConstructor().newInstance();
        String testMethodName = "create_javabean";
        Method method = getMethod(testMethodName, CLAZZ.getDeclaredMethods());
        handlerExecution = new HandlerExecution(invoker, method);

        // when
        ModelAndView result = handlerExecution.handle(request, new MockHttpServletResponse());

        // then
        assertThat(result.getObject("testUser"))
                .usingRecursiveComparison()
                .isEqualTo(new TestUser(
                        "321",
                        "비밀번호",
                        20
                ));
    }

    @DisplayName("요청 URI에 @PathVariable 애노테이션이 붙은 인자값에 할당받은 Controller 메소드를 실행한다.")
    @Test
    void show_pathvariable() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/2");

        Object invoker = CLAZZ.getDeclaredConstructor().newInstance();
        String testMethodName = "show_pathvariable";
        Method method = getMethod(testMethodName, CLAZZ.getDeclaredMethods());
        handlerExecution = new HandlerExecution(invoker, method);

        // when
        ModelAndView result = handlerExecution.handle(request, new MockHttpServletResponse());

        // then
        assertThat(result.getObject("id"))
                .usingRecursiveComparison()
                .isEqualTo(2L);
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
