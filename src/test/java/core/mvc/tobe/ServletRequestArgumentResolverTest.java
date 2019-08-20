package core.mvc.tobe;

import core.mvc.MethodParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServletRequestArgumentResolverTest {

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

    @Test
    void get_argument() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("name", "Summer");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MethodParameter parameter = new MethodParameter("request", HttpServletRequest.class);
        HttpServletRequest argument = (HttpServletRequest) resolver.getMethodArgument(parameter, request, response);

        assertEquals("Summer", argument.getParameter("name"));
    }
}