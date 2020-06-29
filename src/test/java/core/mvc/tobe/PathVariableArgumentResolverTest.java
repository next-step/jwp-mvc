package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PathVariableArgumentResolverTest {

    @Test
    void pathVariable() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/users/1");

        Class clazz = TestUserController.class;
        Method method = getMethod("show_pathvariable", clazz.getDeclaredMethods());

        PathVariableArgumentResolver pathVariableArgumentResolver = new PathVariableArgumentResolver(clazz, method);
        ModelAndView mav = pathVariableArgumentResolver.handle(request, response);
        assertThat(mav.getObject("id")).isInstanceOf(Long.class);
        assertThat(mav.getObject("id")).isEqualTo(1L);
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
