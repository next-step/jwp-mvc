package core.mvc.tobe.method;

import core.di.factory.example.MockView;
import core.mvc.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@DisplayName("서블릿 정보 인자 가져오기")
class ServletWebMethodArgumentResolverTest {

    @Test
    @DisplayName("싱글톤 객체")
    void instance() {
        assertThatNoException()
                .isThrownBy(ServletWebMethodArgumentResolver::instance);
    }

    @Test
    @DisplayName("신규 생성 불가")
    void newInstance_thrownAssertionError() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> ReflectionUtils.newInstance(ServletWebMethodArgumentResolver.class));
    }

    @Test
    @DisplayName("타입이 HttpServletRequest 또는 HttpServletResponse  이면 지원")
    void supportsParameter() throws NoSuchMethodException {
        assertThat(ServletWebMethodArgumentResolver.instance().supportsParameter(servletRequestArgumentMethodParameter())).isTrue();
    }

    @Test
    @DisplayName("HttpServletRequest 타입이 일치하면 그대로 반환")
    void resolveArgument() throws NoSuchMethodException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        //when, then
        assertThat(ServletWebMethodArgumentResolver.instance().resolveArgument(servletRequestArgumentMethodParameter(), request, new MockHttpServletResponse()))
                .isEqualTo(request);
    }

    private MethodParameter servletRequestArgumentMethodParameter() throws NoSuchMethodException {
        Method method = ServletWebMethodArgumentResolverTest.class
                .getDeclaredMethod("servletRequestArgument", HttpServletRequest.class);
        return MethodParameter.of(method, method.getParameters()[0]);
    }

    private ModelAndView servletRequestArgument(HttpServletRequest request) {
        return ModelAndView.from(new MockView());
    }
}
