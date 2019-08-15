package core.mvc.asis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class LegacyMvcHandlerMappingTest {

    private LegacyMvcHandlerMapping legacyMvcHandlerMapping;

    @BeforeEach
    void setUp() {
        legacyMvcHandlerMapping = new LegacyMvcHandlerMapping();
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
        legacyMvcHandlerMapping.initialize();

        //when
        Object handler = legacyMvcHandlerMapping.getHandler(req);

        //then
        assertThat(handler instanceof Controller).isTrue();
    }
}