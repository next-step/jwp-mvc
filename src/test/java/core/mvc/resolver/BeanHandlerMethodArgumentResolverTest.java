package core.mvc.resolver;


import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.tobe.TestUser;
import core.mvc.tobe.TestUserController;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class BeanHandlerMethodArgumentResolverTest {

    private HandlerMethodArgumentResolver beanHandlerMethodArgumentResolver = new BeanHandlerMethodArgumentResolver();
    private MethodParameter beanMethod =new MethodParameter(getMethod("create_javabean", TestUserController.class.getDeclaredMethods()),0);

    @Test
    void supportsParameter_true() {
        Method method = getMethod("create_javabean", TestUserController.class.getDeclaredMethods());
        assertThat(beanHandlerMethodArgumentResolver.supportsParameter(beanMethod)).isEqualTo(true);
    }

    @Test
    void supportsParameter_false() {
        Method method = getMethod("show_pathvariable", TestUserController.class.getDeclaredMethods());
        assertThat(beanHandlerMethodArgumentResolver.supportsParameter(new MethodParameter(method, 0))).isEqualTo(false);
    }

    @Test
    void resolve() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users");
        request.addParameter("userId","test");
        request.addParameter("password", "pp");
        request.addParameter("age", "10");

        TestUser expect = new TestUser(
            "test",
            "pp",
            10
        );

        assertThat(beanHandlerMethodArgumentResolver.resolve(beanMethod, "/users", request, new MockHttpServletResponse()))
            .isEqualTo(expect);
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
            .filter(method -> method.getName().equals(name))
            .findFirst()
            .get();
    }
}
