package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import core.mvc.tobe.view.JspView;
import core.mvc.tobe.view.RedirectView;
import next.controller.ListUserController;
import next.controller.LoginController;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestAdapterTest {

    @Test
    public void executeRedirect() throws Exception {
        Controller controller = new ListUserController();

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        RequestHandlerAdapter adapter = new RequestHandlerAdapter(controller);
        ModelAndView modelAndView = adapter.handle(request, response);

        assertThat(modelAndView.getView()).isEqualToComparingFieldByField(new RedirectView("/users/loginForm"));
    }

    @Test
    public void executeJsp() throws Exception {
        Controller controller = new LoginController();

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        RequestHandlerAdapter adapter = new RequestHandlerAdapter(controller);
        ModelAndView modelAndView = adapter.handle(request, response);

        assertThat(modelAndView.getView()).isEqualToComparingFieldByField(new JspView("/user/login.jsp"));
    }
}
