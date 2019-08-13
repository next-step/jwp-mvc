package next.controller;

import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import core.mvc.asis.DispatcherServlet;

public class ForwardControllerTest {
	private static final String HTTP_METHOD = "GET";
	private static final String REQUEST_URI_USER_FORM = "/users/form";
	private static final String REQUEST_URI_LOGIN_FORM = "/users/loginForm";
	
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    public void setup() throws ServletException {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @DisplayName("변경된 Forwrad 컨트롤러 테스트 : /users/form")
    @Test
    public void usersForm() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI_USER_FORM);
        MockHttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("변경된 Forwrad 컨트롤러 테스트 : /users/loginForm")
    @Test
    public void usersLoginForm() throws Exception {
    	MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI_LOGIN_FORM);
        MockHttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}
