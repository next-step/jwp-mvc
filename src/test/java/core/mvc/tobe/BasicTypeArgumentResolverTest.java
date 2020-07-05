package core.mvc.tobe;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class BasicTypeArgumentResolverTest {

    @Test
    void string() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String userId = "javajigi";
        String password = "password";
        request.addParameter("userId", userId);
        request.addParameter("password", password);

        Class clazz = TestUserController.class;
        Method method = getMethod("create_string", clazz.getDeclaredMethods());

        BasicTypeArgumentResolver basicTypeArgumentResolver = new BasicTypeArgumentResolver();
        boolean sameType = basicTypeArgumentResolver.support(String.class, method);

        Object userIdActual = basicTypeArgumentResolver.getParameterValue(request, response, new ResolverParameter(method, String.class, "userId"));
        assertThat(sameType).isTrue();
        assertThat(userIdActual).isEqualTo(userId);
    }

    @Test
    void intAndLong() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        long id = 1L;
        int age = 29;
        request.setParameter("id", String.valueOf(id));
        request.setParameter("age", String.valueOf(age));

        Class clazz = TestUserController.class;
        Method method = getMethod("create_int_long", clazz.getDeclaredMethods());

        BasicTypeArgumentResolver basicTypeArgumentResolver = new BasicTypeArgumentResolver();
        boolean sameType = basicTypeArgumentResolver.support(long.class, method);

        Object idActual = basicTypeArgumentResolver.getParameterValue(request, response, new ResolverParameter(method, long.class, "id"));

        assertThat(sameType).isTrue();
        assertThat(idActual).isEqualTo(id);

    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }

}
