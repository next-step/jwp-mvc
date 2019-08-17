package core.mvc.asis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class LegacyHandlerMappingTest {

    private LegacyHandlerMapping legacyHandlerMapping;

    @BeforeEach
    void setUp() {
        legacyHandlerMapping = new LegacyHandlerMapping();
    }

    @DisplayName("HandlerMapping 테스트")
    @ParameterizedTest
    @CsvSource({"GET,/"
            ,"GET,/users/form"
            ,"GET,/users/loginForm"
            ,"GET,/users/updateForm"
            ,"POST,/users/update"})
    void getHandler(String method, String requestUri) {
        //given
        HttpServletRequest req = new MockHttpServletRequest(method, requestUri);
        legacyHandlerMapping.initialize();

        //when
        Object handler = legacyHandlerMapping.getHandler(req);

        //then
        assertThat(handler instanceof Controller).isTrue();
    }
}