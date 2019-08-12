package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import core.mvc.asis.DispatcherServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;

import static org.assertj.core.api.Assertions.assertThat;

public class HomeControllerTest {
	private static final String HTTP_METHOD = "GET";
	private static final String REQUEST_URI = "/";
	private static final String USERS_ATTRIBUTE_NAME = "users";
	
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    public void setup() throws ServletException {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @DisplayName("변경된 Home 컨트롤러 테스트")
    @Test
    public void getHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI);
        MockHttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(request.getAttribute(USERS_ATTRIBUTE_NAME)).isNotNull();
    }

    @DisplayName("변경된 Home 컨트롤러 RequestMethod 미지정 테스트 : {0}")
    @ParameterizedTest
    @EnumSource(value = RequestMethod.class)
    public void getHandler(RequestMethod method) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method.name(), REQUEST_URI);
        MockHttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(request.getAttribute(USERS_ATTRIBUTE_NAME)).isNotNull();
    }

}
