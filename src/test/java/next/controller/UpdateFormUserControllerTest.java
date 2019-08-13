package next.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;

import javax.servlet.ServletException;

import org.assertj.core.api.AbstractThrowableAssert;
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

public class UpdateFormUserControllerTest {
	private static final String HTTP_METHOD = "GET";
	private static final String REQUEST_URI_UPDATE_FORM = "/users/updateForm";
	private static final String USER_ID = "userId";
	private static final String TEST_NAME = "circlee";
	private static final String TEST_EMAIL = "circlee7@gmail.com";
	private static final User TEST_USER = new User(TEST_NAME, TEST_NAME, TEST_NAME, TEST_EMAIL);
	
	private DispatcherServlet dispatcherServlet;

	@BeforeEach
	public void setup() throws ServletException, IOException {
		dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.init();

		User user = DataBase.findUserById(TEST_NAME);
		if(user == null) {
			DataBase.addUser(TEST_USER);
		}
	}

	@DisplayName("변경된 UpdateForm 컨트롤러 테스트 : 로그인 유저")
	@Test
	public void myUpdateForm() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(UserSessionUtils.USER_SESSION_KEY, TEST_USER);
		MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI_UPDATE_FORM);
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setSession(session);
		request.setParameter(USER_ID, TEST_NAME);

		dispatcherServlet.service(request, response);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(request.getAttribute("user")).isNotNull();
	}

	@DisplayName("변경된 UpdateForm 컨트롤러 테스트 : 다른 유저")
	@Test
	public void otherUpdateForm() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(UserSessionUtils.USER_SESSION_KEY, TEST_USER);
		MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI_UPDATE_FORM);
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setSession(session);
		request.setParameter(USER_ID, "asd");


		AbstractThrowableAssert<?, ? extends Throwable> assertResult = assertThatThrownBy(() -> {
			dispatcherServlet.service(request, response);
		});
		assertResult.isInstanceOf(ServletException.class);
	}
}
