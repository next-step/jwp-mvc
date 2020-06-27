package core.mvc.resolver;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.tobe.TestUserController;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class ParameterHandlerMethodArgumentResolverTest {


    private HandlerMethodArgumentResolver handlerMethodArgumentResolver = new ParameterHandlerMethodArgumentResolver();

    @Test
    void support(){
        Method method = getMethod("create_string", TestUserController.class.getDeclaredMethods());
        assertThat(handlerMethodArgumentResolver.supportsParameter(new MethodParameter(method,0))).isEqualTo(true);
    }

    @Test
    void getParmater(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users");
        request.addParameter("userId","test");
        request.addParameter("password", "pp");

        Method method = getMethod("create_string", TestUserController.class.getDeclaredMethods());
        assertThat(handlerMethodArgumentResolver.resolve(new MethodParameter(method,0), "/users", request)).isEqualTo("test");
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
            .filter(method -> method.getName().equals(name))
            .findFirst()
            .get();
    }
}
