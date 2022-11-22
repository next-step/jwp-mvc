package next.controller;

import core.mvc.view.ForwardView;
import core.mvc.view.ModelAndView;
import core.mvc.asis.DispatcherServlet;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LoginControllerTest AnnotationMapping Test")
class LoginControllerTest {

    private AnnotationHandlerMapping handlerMapping;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("next.controller");
        handlerMapping.initialize();

        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @Test
    @DisplayName("Annotation Mapping 으로 /form API 요청을 처리한다.")
    void forward_form() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/loginForm");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView modelAndView = execution.handle(request, response);
        ForwardView view = (ForwardView) modelAndView.getView();

        assertThat(view).isEqualTo(new ForwardView("/user/login.jsp"));
    }

    @Test
    @DisplayName("DispatcherServlet 으로 /form API 요청을 처리한다.")
    void forward_form_by_dispatcher() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/loginForm");
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);
    }
}
