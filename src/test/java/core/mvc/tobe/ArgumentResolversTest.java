package core.mvc.tobe;

import next.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class ArgumentResolversTest {
    private ArgumentResolvers argumentResolvers;

    @BeforeEach
    void setUp() {
        argumentResolvers = new ArgumentResolvers();
    }

    @Test
    void beanType() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("userId", "ju");
        request.setParameter("password", "123");
        request.setParameter("age", "29");

        Class clazz = TestUserController.class;
        Method method = getMethod("create_javabean", clazz.getDeclaredMethods());


        final Object[] parameterValues = argumentResolvers.getParameterValues(method, request, response);
        System.out.println(Arrays.toString(parameterValues));
    }

    @Test
    void pathType() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/users/1");

        Class clazz = TestUserController.class;
        Method method = getMethod("show_pathvariable", clazz.getDeclaredMethods());

        final Object[] parameterValues = argumentResolvers.getParameterValues(method, request, response);
        System.out.println(Arrays.toString(parameterValues));
    }

    @Test
    void typeCheck() {
        Class parameterType = UserDao.class;
        assertThat(parameterType.isInstance(String.class)).isFalse();
        assertThat(parameterType.isPrimitive()).isFalse();
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
