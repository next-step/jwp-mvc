package core.mvc.resolver;

import core.mvc.MethodParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;

class SessionArgumentResolverTest {

    private SessionArgumentResolver resolver;
    private MethodParameter parameter;

    @BeforeEach
    void setup() {
        resolver = new SessionArgumentResolver();
        parameter = new MethodParameter("session", HttpSession.class);
    }

    @Test
    void supports() {
        assertTrue(resolver.supports(parameter));
    }

    @DisplayName("Type이 HttpSession인 경우")
    @Test
    void get_argument_session() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        MockHttpServletResponse response = new MockHttpServletResponse();
        HttpSession argument = (HttpSession) resolver.getMethodArgument(parameter, request, response);

        assertEquals(session.getId(), argument.getId());
    }
}