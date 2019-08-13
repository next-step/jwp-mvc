package next.controller;

import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import core.db.DataBase;
import core.mvc.asis.DispatcherServlet;
import next.model.User;

public class CreateUserControllerTest {
	private static final String HTTP_METHOD = "GET";
	private static final String REQUEST_URI = "/users/create";
	
	private static final String TEST_NAME = "circlee";
	private static final String TEST_EMAIL = "circlee7@gmail.com";
	private static final String USER_ID = "userId";
	private static final String PASSWORD = "password";
	private static final String NAME = "name";
	private static final String EMAIL = "email";
	
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    public void setup() throws ServletException {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @DisplayName("변경된 CreateUser 컨트롤러 테스트")
    @Test
    public void createUser() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI);
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter(USER_ID, TEST_NAME);
        request.setParameter(PASSWORD, TEST_NAME);
        request.setParameter(NAME, TEST_NAME);
        request.setParameter(EMAIL, TEST_EMAIL);
        
        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FOUND.value());
        
        User createdUser = DataBase.findUserById(TEST_NAME);
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getName()).isEqualTo(TEST_NAME);
    }
}
