package core.mvc.supporter;


import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;

class SessionMethodArgumentResolverTest {

    @Test
    void resolveArgument() throws Exception {
        // given
        String name = "tester";
        SessionMethodArgumentResolver resolver = new SessionMethodArgumentResolver();

        MockHttpSession httpSession = new MockHttpSession();
        httpSession.setAttribute("name", name);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(httpSession);

        Method method = SessionMethodArgumentResolverTest.class.getMethod("test", HttpSession.class);
        Parameter parameter = method.getParameters()[0];

        // when
        HttpSession actual = (HttpSession) resolver.resolveArgument(parameter, method, request, 0);

        // then
        assertThat(actual.getAttribute("name")).isEqualTo(name);
    }

    public void test(HttpSession httpSession) {
    }

}