package core.mvc.tobe;

import core.mvc.asis.DispatcherServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;

import static org.assertj.core.api.Assertions.assertThat;

public class LegacyRequestMappingTest {
	private static final String HTTP_METHOD = "GET";
	private static final String REQUEST_URI = "/users";
	private static final String NOT_LOGINED_PATH = "/users/loginForm";
	
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    public void setup() throws ServletException {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @DisplayName("Legacy ListUserController 테스트 : not logined")
    @Test
    public void getListUserControllerResult() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI);
        MockHttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getHeader(HttpHeaders.LOCATION)).isEqualTo(NOT_LOGINED_PATH);
    }

}
