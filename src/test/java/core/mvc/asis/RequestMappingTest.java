package core.mvc.asis;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Request mapping 클래스")
class RequestMappingTest {
    private static final RequestMapping requestMapping = new RequestMapping();

    @BeforeAll
    static void init() {
        requestMapping.initMapping();
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("핸들러 찾기")
    void getHandler(final String uri) {
        assertThat(requestMapping.getHandler(new MockHttpServletRequest("GET", uri)))
                .isNotNull();
    }

    // 이런 테스트는 할필요가 있을까..? 수정에 따라 테스트가 무조건 깨지는데..
    private static Stream<String> getHandler() {
        return Stream.of(
                "/",
                "/users/form",
                "/users/loginForm",
                "/users",
                "/users/login",
                "/users/logout",
                "/users/update"
        );
    }

    @Test
    @DisplayName("핸들러 찾기 실패할 경우 null 반환")
    void getHandlerNotExist() {
        assertThat(requestMapping.getHandler(new MockHttpServletRequest("GET", "/not-exist")))
                .isNull();
    }
}
