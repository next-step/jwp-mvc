package core.mvc.tobe;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class BeanTypeArgumentResolverTest {

    @Test
    void beanType() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setParameter("userId", "ju");
        request.setParameter("password", "123");
        request.setParameter("age", "29");

        Class clazz = TestUserController.class;
        Method method = getMethod("create_javabean", clazz.getDeclaredMethods());

        BeanTypeArgumentResolver beanTypeArgumentResolver = new BeanTypeArgumentResolver();
        boolean sameType = beanTypeArgumentResolver.equalsTo(String.class, method);
        boolean same = beanTypeArgumentResolver.equalsTo(TestUser.class, method);
        final Object value = beanTypeArgumentResolver.getParameterValue(request, response, new ResolverParameter(method, TestUser.class, null));

        assertThat(sameType).isFalse();
        assertThat(same).isTrue();
        assertThat(value).isInstanceOf(TestUser.class);

    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
