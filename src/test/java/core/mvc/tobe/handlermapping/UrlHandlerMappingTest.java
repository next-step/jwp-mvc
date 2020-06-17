package core.mvc.tobe.handlermapping;

import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handler.HandlerExecutions;
import core.mvc.tobe.handlermapping.custom.UrlHandlerMapping;
import next.controller.LoginController;
import next.controller.ProfileController;
import next.controller.UpdateFormUserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlHandlerMappingTest {
    private final UrlHandlerMapping handlerMapping = new UrlHandlerMapping();
    private HandlerExecutions handlerExecutions;

    @BeforeEach
    public void setup() {
        handlerExecutions = handlerMapping.init();
    }

    @DisplayName("init() 메소드 호출 - UrlMappingHandler (handler 존재)")
    @ParameterizedTest
    @ValueSource(strings = {"/users", "/users/login", "/users/loginForm"})
    void initForUrlHandlerMapping(String url) throws ServletException, IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest(null, url);

        //when
        UrlHandlerMapping urlHandlerMapping = new UrlHandlerMapping();
        HandlerExecutions handlerExecutions = urlHandlerMapping.init();

        //then
        HandlerExecution handler = handlerExecutions.findHandlerByUrl(request.getRequestURI());
        assertThat(handler).isNotNull();
    }

    @DisplayName("init() 메소드 호출 - UrlMappingHandler (handler 존재 X)")
    @ParameterizedTest
    @ValueSource(strings = {"/users/search", "/member/login"})
    void initForUrlHandlerMappingWhenNotMatched(String url) throws ServletException, IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest(null, url);

        //when
        UrlHandlerMapping urlHandlerMapping = new UrlHandlerMapping();
        HandlerExecutions handlerExecutions = urlHandlerMapping.init();

        //then
        HandlerExecution handler = handlerExecutions.findHandlerByUrl(url);
        assertThat(handler).isNull();
    }

    @DisplayName("Request 객체 를 입력하면, mapping 된 컨트롤러를 가진 HandlerExecution 반환")
    @Test
    void findHandler() {
        //given
        MockHttpServletRequest loginRequest = new MockHttpServletRequest(null, "/users/login");
        MockHttpServletRequest profileRequest = new MockHttpServletRequest(null, "/users/profile");
        MockHttpServletRequest updateFormRequest = new MockHttpServletRequest(null, "/users/updateForm");

        //when
        HandlerExecution loginHandler = handlerMapping.findHandler(loginRequest);
        HandlerExecution profileHandler = handlerMapping.findHandler(profileRequest);
        HandlerExecution updateFormHandler = handlerMapping.findHandler(updateFormRequest);

        //then
        assertThat(loginHandler.getController()).isInstanceOf(LoginController.class);
        assertThat(profileHandler.getController()).isInstanceOf(ProfileController.class);
        assertThat(updateFormHandler.getController()).isInstanceOf(UpdateFormUserController.class);
    }

    @DisplayName("요청 uri에 매핑된 컨트롤러가 있으면 true, 없으면 false 반환")
    @ParameterizedTest
    @CsvSource(value = {"/users/login:true", "/users/search:false"}, delimiter = ':')
    void hasHandler(String requestUri, boolean expected) {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest(null, requestUri);

        //when
        boolean hasHandler = handlerMapping.hasHandler(request);

        //then
        assertThat(hasHandler).isEqualTo(expected);
    }
}
