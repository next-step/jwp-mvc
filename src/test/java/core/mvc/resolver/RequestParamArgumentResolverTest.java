package core.mvc.resolver;

import static org.assertj.core.api.Assertions.assertThat;

import core.annotation.web.RequestParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class RequestParamArgumentResolverTest {

    private static final ArgumentResolver argumentResolver = new RequestParamArgumentResolver();
    private MockHttpServletRequest request;
    private RequestParam annotation;

    @BeforeEach
    void setup() throws NoSuchMethodException {
        request = new MockHttpServletRequest();
    }

    @DisplayName("String 을 RequestParam 으로 resolve 가능해야한다.")
    @Test
    void resolveString() throws NoSuchMethodException {
        String paramName = "user";
        String argument = "Pyro";
        request.setParameter(paramName, argument);

        Method method = MockArgumentResolverController.class
            .getDeclaredMethod("requestParamStringMethod", String.class);
        Object actual = argumentResolver.resolveArgument(
            new MethodParameter(method, String.class, new Annotation[]{
                method.getParameterAnnotations()[0][0]
            }, paramName)
            , request
            , new MockHttpServletResponse()
        );

        String expected = argument;
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("int 를 RequestParam 으로 resolve 가능해야한다.")
    @Test
    void resolveInteger() throws NoSuchMethodException {
        String paramName = "id";
        String argument = "24";
        request.setParameter(paramName, argument);

        Method method = MockArgumentResolverController.class
            .getDeclaredMethod("requestParamIntegerMethod", int.class);
        Object actual = argumentResolver.resolveArgument(
            new MethodParameter(
                method,
                int.class,
                new Annotation[]{method.getParameterAnnotations()[0][0]}
                , paramName
            ), request
            , new MockHttpServletResponse()
        );

        int expected = Integer.parseInt(argument);
        assertThat(actual).isEqualTo(expected);
    }

}
