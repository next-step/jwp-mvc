package core.mvc.tobe.argumentResolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HttpServletRequestArgumentResolverTest {

    @DisplayName("전달된 파라미터의 타입이 HttpServletRequest인지 확인한다.")
    @Test
    void supportParameter() {
        HandlerMethodArgumentResolver resolver = new HttpServletRequestArgumentResolver();
        assertThat(resolver.supportsParameter(HttpServletRequest.class)).isTrue();
    }

    @DisplayName("HttpServletRequest 인스턴스를 입력하면 HttpServletRequest 인스턴스가 결과로 나온다.")
    @Test
    void resolver() {
        HandlerMethodArgumentResolver resolver = new HttpServletRequestArgumentResolver();
        List<Object> expected = new ArrayList<>();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        expected.add(request);
        expected.add(response);
        assertThat(resolver.resolveArgument(MockHttpServletRequest.class, expected)).isEqualTo(request);
    }
}