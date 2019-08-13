package next.controller;

import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import core.mvc.asis.DispatcherServlet;
import next.model.User;

public class ListUserControllerTest {
	private static final String HTTP_METHOD = "GET";
	private static final String REQUEST_URI = "/users";
	private static final String USERS_ATTRIBUTE_NAME = "users";
	private static final String TEST_NAME = "circlee";
	private static final String TEST_EMAIL = "circlee7@gmail.com";
	
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    public void setup() throws ServletException {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @DisplayName("변경된 ListUser 컨트롤러 테스트 : 로그인 x")
    @Test
    public void testListUserController() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI);
        MockHttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getHeader(HttpHeaders.LOCATION)).isEqualTo("/users/loginForm");
    }

    @DisplayName("변경된 ListUser 컨트롤러 테스트 : 로그인 o")
    @Test
    public void testListUserControllerLogin() throws Exception {
    	User user = new User(TEST_NAME, TEST_NAME, TEST_NAME, TEST_EMAIL);
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
    	MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI);
    	request.setSession(session);
        MockHttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(request.getAttribute(USERS_ATTRIBUTE_NAME)).isNotNull();
    }

}
