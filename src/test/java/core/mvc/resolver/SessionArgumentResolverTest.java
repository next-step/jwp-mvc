package core.mvc.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.lang.reflect.Method;
import java.util.List;

import static core.mvc.resolver.ResolverUtils.generateMethodParameters;
import static core.mvc.resolver.ResolverUtils.getMethod;
import static org.junit.jupiter.api.Assertions.*;

class SessionArgumentResolverTest {

    private static final SessionArgumentResolver resolver = new SessionArgumentResolver();

    @DisplayName("SessionArgumentResolver는 Session 타입을 지원한다.")
    @Test
    void supportParameter() throws NoSuchMethodException {
        Method session = getMethod("session");
        List<MethodParameter> methodParameters = generateMethodParameters(session);

        methodParameters.forEach(methodParameter -> assertTrue(resolver.supportsParameter(methodParameter)));
    }

    @DisplayName("SessionArgumentResolver는 Session 타입을 resolve한다.")
    @Test
    void resolveArgument() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession httpSession = new MockHttpSession();
        request.setSession(httpSession);

        Method session = getMethod("session");
        List<MethodParameter> methodParameters = generateMethodParameters(session);

        methodParameters.forEach(methodParameter -> assertEquals(httpSession, resolver.resolveArgument(methodParameter, request)));
    }

}
