package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @DisplayName("매핑되는 핸들러가 있을 경우 성공한다")
    @ParameterizedTest
    @CsvSource({
            "'GET', '/users'",
            "'POST', '/users'",
            "'GET', '/users/findUserId'",
    })
    void handle_isExistRequest_isNotNull(String method, String url) {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest(method, url);

        // when
        ModelAndView handler = handlerMapping.handle(request, new MockHttpServletResponse());

        // then
        assertThat(handler).isNotNull();
    }

    @DisplayName("매핑되는 핸들러가 없을 경우 exception")
    @ParameterizedTest
    @CsvSource({
            "'GET', '/none'",
            "'POST', '/none'",
    })
    void handle_isNotExistRequest_exception(String method, String url) {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest(method, url);

        // when && exception
        assertThatExceptionOfType(NotFoundServletException.class)
                .isThrownBy(() -> handlerMapping.handle(request, new MockHttpServletResponse()));
    }

    @DisplayName("매핑되는 핸들러가 있는지 확인한다")
    @ParameterizedTest
    @CsvSource({
            "'GET', '/users', 'true'",
            "'GET', '/none', 'false'",
    })
    void isExistHandler(String method, String url, boolean result) {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest(method, url);

        // when
        boolean existHandler = handlerMapping.isExistHandler(request);

        // then
        assertThat(existHandler).isEqualTo(result);
    }
}