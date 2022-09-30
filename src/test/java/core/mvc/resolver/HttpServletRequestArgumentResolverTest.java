package core.mvc.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;

import static core.mvc.resolver.ResolverUtils.generateMethodParameters;
import static core.mvc.resolver.ResolverUtils.getMethod;
import static org.junit.jupiter.api.Assertions.*;

class HttpServletRequestArgumentResolverTest {

    private final HttpServletRequestArgumentResolver resolver = new HttpServletRequestArgumentResolver();

    @DisplayName("HttpServletRequestArgumentResolver는 httpServletRequest 타입을 지원한다.")
    @Test
    void supportParameter() throws NoSuchMethodException {
        Method method = getMethod("httpServletRequest");

        generateMethodParameters(method)
                .forEach(methodParameter -> assertEquals(true, resolver.supportsParameter(methodParameter)));
    }

    @DisplayName("HttpServletRequestArgumentResolver는 httpServletRequest 타입을 resolve한다.")
    @Test
    void resolveArgument() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Method method = getMethod("httpServletRequest");
        generateMethodParameters(method)
                .forEach(methodParameter -> {
                    Object actual = resolver.resolveArgument(methodParameter, request);
                    assertEquals(request, actual);
                });
    }
}
