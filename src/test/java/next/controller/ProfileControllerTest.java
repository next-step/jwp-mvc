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

import core.db.DataBase;
import core.mvc.asis.DispatcherServlet;
import next.model.User;

public class ProfileControllerTest {
	private static final String HTTP_METHOD = "GET";
	private static final String REQUEST_URI_PROFILE = "/users/profile";
	private static final String TEST_NAME = "circlee";
	private static final String TEST_EMAIL = "circlee7@gmail.com";
	private static final String USER_ID = "userId";


	private DispatcherServlet dispatcherServlet;

	@BeforeEach
	public void setup() throws ServletException, IOException {
		dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.init();
		
		User user = DataBase.findUserById(TEST_NAME);
		if(user == null) {
			User newUser = new User(TEST_NAME, TEST_NAME, TEST_NAME, TEST_EMAIL);
			DataBase.addUser(newUser);
		}
	}

	@DisplayName("변경된 Login 컨트롤러 테스트 : 로그인 세션 존재")
	@Test
	public void memberLogout() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI_PROFILE);
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setParameter(USER_ID, TEST_NAME);
		
		dispatcherServlet.service(request, response);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(request.getAttribute("user")).isNotNull();
	}
}
