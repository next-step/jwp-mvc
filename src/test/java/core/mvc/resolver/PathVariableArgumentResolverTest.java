package core.mvc.resolver;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class PathVariableArgumentResolverTest {
    private static final ArgumentResolver argumentResolver = new PathVariableArgumentResolver();

    @DisplayName("String 을 Path Variable 로 resolve 가능해야한다.")
    @Test
    void resolveString() throws NoSuchMethodException {
        String paramName = "user";
        String argument = "pyro";

        Method method = MockArgumentResolverController.class
            .getDeclaredMethod("pathVariableStringMethod", String.class);
        Object actual = argumentResolver.resolveArgument(
            new MethodParameter(method, String.class, new Annotation[]{
                method.getParameterAnnotations()[0][0]
            }, paramName)
            , new MockHttpServletRequest("GET", "/pathVariable/" + argument)
            , new MockHttpServletResponse()
        );

        String expected = argument;
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("int 를 Path Variable 로 resolve 가능해야한다.")
    @Test
    void resolveInteger() throws NoSuchMethodException {
        String paramName = "id";
        String argument = "24";

        Method method = MockArgumentResolverController.class
            .getDeclaredMethod("pathVariableIntegerMethod", int.class);
        Object actual = argumentResolver.resolveArgument(
            new MethodParameter(
                method,
                int.class,
                new Annotation[]{method.getParameterAnnotations()[0][0]}
                , paramName
            ), new MockHttpServletRequest("GET", "/pathVariable/user/" + argument)
            , new MockHttpServletResponse()
        );

        int expected = Integer.parseInt(argument);
        assertThat(actual).isEqualTo(expected);
    }
}
