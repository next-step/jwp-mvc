package core.mvc.tobe.argumentResolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HttpServletResponseArgumentResolverTest {

    @DisplayName("전달된 파라미터의 타입이 HttpServletResponse인지 확인한다.")
    @Test
    void supportParameter() {
        HandlerMethodArgumentResolver resolver = new HttpServletResponseArgumentResolver();
        assertThat(resolver.supportsParameter(HttpServletResponse.class)).isTrue();
    }

    @DisplayName("HttpServletResponse 인스턴스를 입력하면 HttpServletResponse 인스턴스가 결과로 나온다.")
    @Test
    void resolver() {
        HandlerMethodArgumentResolver resolver = new HttpServletResponseArgumentResolver();
        List<Object> expected = new ArrayList<>();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        expected.add(request);
        expected.add(response);
        assertThat(resolver.resolveArgument(MockHttpServletResponse.class, expected)).isEqualTo(response);
    }
}