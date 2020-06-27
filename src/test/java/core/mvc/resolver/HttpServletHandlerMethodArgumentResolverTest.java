package core.mvc.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import core.mvc.tobe.TestUserController;
import java.lang.reflect.Method;
import java.util.Arrays;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class HttpServletHandlerMethodArgumentResolverTest {

    private final HttpServletHandlerMethodArgumentResolver httpServletHandlerMethodArgumentResolver = new HttpServletHandlerMethodArgumentResolver();

    @Test
    void supportsParameter_true() {
        Method method = getMethod("httpServlet", TestUserController.class.getDeclaredMethods());
        assertThat(httpServletHandlerMethodArgumentResolver.supportsParameter(new MethodParameter(method,0)))
            .isEqualTo(true);
    }

    @Test
    void supportsParameter_false() {
        Method method = getMethod("show_pathvariable", TestUserController.class.getDeclaredMethods());
        assertThat(httpServletHandlerMethodArgumentResolver.supportsParameter(new MethodParameter(method,0)))
            .isEqualTo(false);
    }

    @Test
    void resolve() {

        Method method = getMethod("httpServlet", TestUserController.class.getDeclaredMethods());
        HttpServletRequest request = new MockHttpServletRequest();

        assertThat(httpServletHandlerMethodArgumentResolver.resolve(new MethodParameter(method,0), "/users", request, new MockHttpServletResponse()))
            .isEqualTo(request);
    }


    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
            .filter(method -> method.getName().equals(name))
            .findFirst()
            .get();
    }
}
