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

import core.mvc.asis.DispatcherServlet;

public class LoginControllerTest {
	private static final String HTTP_METHOD = "GET";
	private static final String REQUEST_URI_CREATE = "/users/create";
	private static final String REQUEST_URI_LOGIN = "/users/login";



	private static final String TEST_NAME = "circlee";
	private static final String TEST_EMAIL = "circlee7@gmail.com";
	private static final String USER_ID = "userId";
	private static final String PASSWORD = "password";
	private static final String NAME = "name";
	private static final String EMAIL = "email";

	private DispatcherServlet dispatcherServlet;

	@BeforeEach
	public void setup() throws ServletException, IOException {
		dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.init();

		MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI_CREATE);
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setParameter(USER_ID, TEST_NAME);
		request.setParameter(PASSWORD, TEST_NAME);
		request.setParameter(NAME, TEST_NAME);
		request.setParameter(EMAIL, TEST_EMAIL);

		dispatcherServlet.service(request, response);
	}

	@DisplayName("변경된 Login 컨트롤러 테스트 : 존재하는 유저")
	@Test
	public void memberLogin() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI_LOGIN);
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setParameter(USER_ID, TEST_NAME);
		request.setParameter(PASSWORD, TEST_NAME);

		dispatcherServlet.service(request, response);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FOUND.value());

		HttpSession session = request.getSession();
		Object userSessionAttribute = session.getAttribute(UserSessionUtils.USER_SESSION_KEY);
		assertThat(userSessionAttribute).isNotNull();
	}
	
	@DisplayName("변경된 Login 컨트롤러 테스트 : 존재하지 않는 유저")
	@Test
	public void notMemeberLogin() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI_LOGIN);
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setParameter(USER_ID, "asd");
		request.setParameter(PASSWORD, "def");

		dispatcherServlet.service(request, response);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(request.getAttribute("loginFailed")).isEqualTo(Boolean.TRUE);
	}
}
