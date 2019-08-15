package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerMappingsTest {

    private HandlerMappings handlerMappings;

    @BeforeEach
    void setUp() {
        handlerMappings = HandlerMappings.of();
    }

    @DisplayName("support 확인 - legacy, annotation")
    @ParameterizedTest
    @CsvSource({"GET, /"
            , "POST, /users/login"})
    void support(String method, String requestUri) {
        //given
        HttpServletRequest req = new MockHttpServletRequest(method, requestUri);

        //when & then
        assertThat(handlerMappings.support(req)).isTrue();
    }
}