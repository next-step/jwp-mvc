package core.mvc.resolver;

import core.mvc.MethodParameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class SessionArgumentResolverTest {
    private SessionArgumentResolver resolver = new SessionArgumentResolver();

    @DisplayName("인자로 세션 정보를 주입받을 수 있다.")
    @Test
    void resolveHttpSession() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resq = new MockHttpServletResponse();
        MethodParameter mp = MethodParameter.of(getMethod("requireSession", MyController.class.getDeclaredMethods()), 0);

        Object value = resolver.resolve(req, resq, "session",mp);

        assertThat(resolver.support(mp)).isTrue();
        assertThat(value).isInstanceOf(HttpSession.class);

    }

    private static Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }


    class MyController {

        public void requireSession(HttpSession session) {

        }

    }
}
