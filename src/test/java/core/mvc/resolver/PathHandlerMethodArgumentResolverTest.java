package core.mvc.resolver;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.TestUserController;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class PathHandlerMethodArgumentResolverTest {

    private HandlerMethodArgumentResolver handlerMethodArgumentResolver = new PathHandlerMethodArgumentResolver();

    @Test
    void support(){
        Method method = getMethod("show_pathvariable", TestUserController.class.getDeclaredMethods());
        assertThat(handlerMethodArgumentResolver.supportsParameter(new MethodParameter(method,0))).isEqualTo(true);
    }

    @Test
    void getParmater(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users/1");

        Method method = getMethod("show_pathvariable", TestUserController.class.getDeclaredMethods());
        assertThat(handlerMethodArgumentResolver.resolve(new MethodParameter(method,0), "/users/{id}", request, new MockHttpServletResponse())).isEqualTo(1L);
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
            .filter(method -> method.getName().equals(name))
            .findFirst()
            .get();
    }
}
