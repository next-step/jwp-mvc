package core.mvc.tobe;

import core.mvc.ModelAndView;
import next.model.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.util.Arrays;

import static core.mvc.tobe.argumentresolver.HandlerMethodArgumentResolverComposite.resolveParameters;
import static org.assertj.core.api.Assertions.assertThat;

class HandlerMethodArgumentResolverTest {
    private static final Logger logger = LoggerFactory.getLogger(HandlerMethodArgumentResolverTest.class);

    @Test
    void show_pathvariable() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        final long id = 12345L;
        request.setServletPath("/users/" + id);

        Class clazz = TestUserController.class;
        Method method = getMethod("show_pathvariable", clazz.getDeclaredMethods());

        Object[] values = resolveParameters(method, request, null);

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("id")).isEqualTo(id);
    }

    @Test
    void create_javabean() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        final String userId = "userId";
        final String password = "password";
        final String name = "name";
        final String email = "email";
        User user = new User(userId, password, name, email);
        request.addParameter("userId", userId);
        request.addParameter("password", password);
        request.addParameter("name", name);
        request.addParameter("email", email);

        Class clazz = TestUserController.class;
        Method method = getMethod("create_javabean", clazz.getDeclaredMethods());

        Object[] values = resolveParameters(method, request, null);

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("user")).isEqualTo(user);
    }

    @Test
    void int_long() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        long id = 9999L;
        int age = 20;
        request.addParameter("id", String.valueOf(id));
        request.addParameter("age", String.valueOf(age));

        Class clazz = TestUserController.class;
        Method method = getMethod("create_int_long", clazz.getDeclaredMethods());

        Object[] values = resolveParameters(method, request, null);

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("id")).isEqualTo(id);
        assertThat(mav.getObject("age")).isEqualTo(age);
    }

    @Test
    void string() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String userId = "javajigi";
        String password = "password";
        request.addParameter("userId", userId);
        request.addParameter("password", password);

        Class clazz = TestUserController.class;
        Method method = getMethod("create_string", clazz.getDeclaredMethods());

        Object[] values = resolveParameters(method, request, null);

        ModelAndView mav = (ModelAndView) method.invoke(clazz.newInstance(), values);
        assertThat(mav.getObject("userId")).isEqualTo(userId);
        assertThat(mav.getObject("password")).isEqualTo(password);
    }

    public static Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
