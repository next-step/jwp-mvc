package next.controller;

import core.db.DataBase;
import core.mvc.tobe.DispatcherServlet;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.view.ForwardView;
import core.mvc.view.ModelAndView;
import core.mvc.view.RedirectView;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpSession;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ListUserControllerTest AnnotationMapping Test")
class ListUserControllerTest {

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
    @DisplayName("Annotation Mapping 으로 로그인 하지 않은 경우의 /users API 요청을 처리한다.")
    void listup_when_without_login() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView modelAndView = execution.handle(request, response);
        RedirectView view = (RedirectView) modelAndView.getView();

        assertThat(view).isEqualTo(new RedirectView("/users/loginForm"));
    }

    @Test
    @DisplayName("Annotation Mapping 으로 로그인 한 경우의 /users API 요청을 처리한다.")
    void listup_when_login() throws Exception {
        User user = new User("admin", "password", "자바지기", "admin@slipp.net");
        DataBase.addUser(user);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        HttpSession session = request.getSession();
        Objects.requireNonNull(session).setAttribute("user", user);

        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView modelAndView = execution.handle(request, response);
        ForwardView view = (ForwardView) modelAndView.getView();

        assertThat(view).isEqualTo(new ForwardView("/user/list.jsp"));
    }
}
