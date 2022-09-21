package core.mvc.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;

import static core.mvc.resolver.ResolverUtils.generateMethodParameters;
import static core.mvc.resolver.ResolverUtils.getMethod;
import static org.junit.jupiter.api.Assertions.*;

class PathVariableArgumentResolverTest {

    private final PathVariableArgumentResolver resolver = new PathVariableArgumentResolver();

    @DisplayName("PathVariableArgumentResolver는 PathVariable 타입을 지원한다.")
    @Test
    void supportParameter() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", "1");

        Method method = getMethod("show_pathvariable");
        generateMethodParameters(method)
                .forEach(methodParameter -> assertTrue(resolver.supportsParameter(methodParameter)));
    }

    @DisplayName("PathVariableArgumentResolver는 PathVariable 타입을 resolve한다.")
    @Test
    void resolveArgument() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users/1");
        request.addParameter("id", "1");

        Method method = getMethod("show_pathvariable");
        generateMethodParameters(method)
                .forEach(methodParameter -> assertEquals(1L, resolver.resolveArgument(methodParameter, request)));
    }
}
