package core.mvc.tobe.method.support;

import core.di.factory.example.MockView;
import core.mvc.ModelAndView;
import core.mvc.tobe.method.MethodParameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@DisplayName("서블릿 인자 추출")
class ServletRequestMethodArgumentResolverTest {

    @DisplayName("HttpServletRequest, HttpServletResponse 지원")
    @Test
    void support_http_servlet() throws NoSuchMethodException {
        assertThat(ServletRequestMethodArgumentResolver.getResolver()
                .supportsParameter(servletRequestArgumentMethodParam())).isTrue();
    }

    @DisplayName("HttpServletRequest 타입 일치시 그대로 반환")
    @Test
    void resolveArgument() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThat(ServletRequestMethodArgumentResolver.getResolver()
                .resolveArgument(servletRequestArgumentMethodParam(), request, new MockHttpServletResponse()))
                .isEqualTo(request);
    }

    private MethodParameter servletRequestArgumentMethodParam() throws NoSuchMethodException {
        Method method = ServletRequestMethodArgumentResolverTest.class
                .getDeclaredMethod("servletRequestArgument", HttpServletRequest.class);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private ModelAndView servletRequestArgument(HttpServletRequest request) {
        return new ModelAndView(new MockView());
    }
}
