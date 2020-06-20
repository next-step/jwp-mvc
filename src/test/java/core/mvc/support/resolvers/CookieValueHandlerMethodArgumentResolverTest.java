package core.mvc.support.resolvers;

import core.annotation.web.CookieValue;
import core.mvc.support.MethodParameter;
import core.mvc.support.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.Cookie;
import java.lang.reflect.Method;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CookieValueHandlerMethodArgumentResolverTest {

    private static final HandlerMethodArgumentResolver resolver = new CookieValueHandlerMethodArgumentResolver();
    private static final String ACCESS_TOKEN_KEY = "sessionId";
    private static final String ACCESS_TOKEN_VALUE = UUID.randomUUID().toString();

    private MethodParameter methodParameter;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() throws Exception {
        final Method method = CookieValueHandlerMethodArgumentResolverTest
                .class
                .getMethod("testMethod", String.class);
        final MethodSignature methodSignature = new MethodSignature(method);
        methodParameter = methodSignature.getMethodParameters().get(0);

        request = new MockHttpServletRequest("GET", "/users");
        final Cookie yummyCookie = new Cookie(ACCESS_TOKEN_KEY, ACCESS_TOKEN_VALUE);
        request.setCookies(yummyCookie);
    }

    @DisplayName("CookieValue가 붙은 친구를 지원하는지 검사한다!")
    @Test
    void test_support_condition() {
        assertTrue(resolver.supportsParameter(methodParameter));
    }

    @DisplayName("쿠키 값을 검사해보자.")
    @Test
    void test_get_cookie_value() {
        final String resolvedSessionId = (String) resolver.resolveArgument(methodParameter, request);
        assertThat(resolvedSessionId).isEqualTo(ACCESS_TOKEN_VALUE);
    }

    public void testMethod(@CookieValue(ACCESS_TOKEN_KEY) String sessionId) {
        // no-op
    }
}