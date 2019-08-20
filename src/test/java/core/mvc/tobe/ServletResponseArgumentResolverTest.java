package core.mvc.tobe;

import core.mvc.MethodParameter;
import core.mvc.resolver.ServletRequestArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ServletResponseArgumentResolverTest {
    private ServletRequestArgumentResolver resolver;
    private MethodParameter parameter;

    @BeforeEach
    void setup() {
        resolver = new ServletRequestArgumentResolver();
        parameter = new MethodParameter("request", HttpServletRequest.class);
    }

    @Test
    void supports() {
        assertTrue(resolver.supports(parameter));
    }
}