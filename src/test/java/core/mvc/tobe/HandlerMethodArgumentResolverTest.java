package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.ModelAndView;
import core.mvc.args.MethodParameters;
import core.mvc.args.resolver.MethodArgumentResolvers;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

public class HandlerMethodArgumentResolverTest {

    private static final Logger logger = LoggerFactory.getLogger(HandlerMethodArgumentResolverTest.class);

    @DisplayName("int, long 형식의 파라미터 resolve 테스트")
    @Test
    void int_long() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Long id = 1L;
        int age = 20;
        request.addParameter("id", String.valueOf(id));
        request.addParameter("age", String.valueOf(age));

        Class clazz = TestUserController.class;
        Method method = getMethod("create_int_long", clazz.getDeclaredMethods());
        MethodParameters methodParameters = new MethodParameters(method);
        Object[] values = MethodArgumentResolvers.resolveArguments(methodParameters, request);

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("id")).isEqualTo(id);
        assertThat(mav.getObject("age")).isEqualTo(age);
    }

    @DisplayName("string 형식의 파라미터 resolve 테스트")
    @Test
    void string() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String userId = "javajigi";
        String password = "password";
        request.addParameter("userId", userId);
        request.addParameter("password", password);

        Class clazz = TestUserController.class;
        Method method = getMethod("create_string", clazz.getDeclaredMethods());
        MethodParameters methodParameters = new MethodParameters(method);
        Object[] values = MethodArgumentResolvers.resolveArguments(methodParameters, request);

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("userId")).isEqualTo(userId);
        assertThat(mav.getObject("password")).isEqualTo(password);
    }

    @DisplayName("PathVariable 형식의 파라미터 resolve 테스트")
    @Test
    void pathVariable() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        long id = 1;
        request.setRequestURI("/users/" + id);

        Class clazz = TestUserController.class;
        Method method = getMethod("show_pathvariable", clazz.getDeclaredMethods());
        MethodParameters methodParameters = new MethodParameters(method);
        Object[] values = MethodArgumentResolvers.resolveArguments(methodParameters, request);

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("id")).isEqualTo(id);
    }

    @DisplayName("Bean 형식의 파라미터 resolve 테스트")
    @Test
    void beanVariable() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String userId = "java";
        String password = "password";
        int age = 20;
        TestUser testUser = new TestUser(userId, password, age);
        request.setRequestURI("/users");
        request.addParameter("userId", userId);
        request.addParameter("password", password);
        request.addParameter("age", String.valueOf(age));

        Class clazz = TestUserController.class;
        Method method = getMethod("create_javabean", clazz.getDeclaredMethods());
        MethodParameters methodParameters = new MethodParameters(method);
        Object[] values = MethodArgumentResolvers.resolveArguments(methodParameters, request);

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("testUser")).isEqualTo(testUser);
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
            .filter(method -> method.getName().equals(name))
            .findFirst()
            .get();
    }
}
