package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import core.mvc.tobe.handler.HandlerKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerKeyTest {
    @DisplayName("method 가 없는 요청은, 모든 method를 할당해 HandlerKey를 생성한다.")
    @Test
    void createByRequest() {
        //given
        String requestMethod = null;
        String url = "/users";
        MockHttpServletRequest request = new MockHttpServletRequest(requestMethod, url);

        //when
        HandlerKey handlerKey = HandlerKey.of(request);

        //then
        assertThat(handlerKey.getRequestMethod()).hasSize(RequestMethod.values().length);
    }
}
