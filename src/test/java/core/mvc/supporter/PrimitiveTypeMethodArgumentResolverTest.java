package core.mvc.supporter;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;

class PrimitiveTypeMethodArgumentResolverTest {

    @Test
    void resolveArgument() throws Exception {
        // given
        String name = "tester";
        int age = 20;
        PrimitiveTypeMethodArgumentResolver resolver = new PrimitiveTypeMethodArgumentResolver();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", name);
        request.setParameter("age", String.valueOf(age));

        Method method = PrimitiveTypeMethodArgumentResolverTest.class.getMethod("test", String.class, int.class);
        Parameter parameter1 = method.getParameters()[0];
        Parameter parameter2 = method.getParameters()[1];

        // when
        String actual = (String) resolver.resolveArgument(parameter1, method, request, 0);
        Integer actual2 = (Integer) resolver.resolveArgument(parameter2, method, request, 1);

        // then
        assertThat(actual).isEqualTo(name);
        assertThat(actual2).isEqualTo(age);
    }

    public void test(String name, int age) {
    }
}
