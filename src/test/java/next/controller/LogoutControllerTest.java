package next.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import core.mvc.asis.DispatcherServlet;
import next.model.User;

public class LogoutControllerTest {
	private static final String HTTP_METHOD = "GET";
	private static final String REQUEST_URI_LOGOUT = "/users/logout";
	private static final String TEST_NAME = "circlee";
	private static final String TEST_EMAIL = "circlee7@gmail.com";

	private DispatcherServlet dispatcherServlet;

	@BeforeEach
	public void setup() throws ServletException, IOException {
		dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.init();
	}

	@DisplayName("변경된 Login 컨트롤러 테스트 : 로그인 세션 존재")
	@Test
	public void memberLogout() throws Exception {
		User user = new User(TEST_NAME, TEST_NAME, TEST_NAME, TEST_EMAIL);
    	MockHttpSession session = new MockHttpSession();
    	session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
		MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI_LOGOUT);
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setSession(session);
		
		dispatcherServlet.service(request, response);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FOUND.value());
		HttpSession afterSession = request.getSession();
		Object userSessionAttribute = afterSession.getAttribute(UserSessionUtils.USER_SESSION_KEY);
		assertThat(userSessionAttribute).isNull();
	}
}
