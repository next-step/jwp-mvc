package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import core.mvc.asis.Controller;
import next.controller.ListUserController;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestHandlerCommandTest {

    @Test
    public void execute() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Controller controller = new ListUserController();
        HandlerCommand handlerCommand = new RequestHandlerCommand(request, response);
        ModelAndView modelAndView = handlerCommand.execute(controller);

        assertThat(modelAndView.getView()).isEqualToComparingFieldByField(new RedirectView("redirect:/users/loginForm"));
    }
}
