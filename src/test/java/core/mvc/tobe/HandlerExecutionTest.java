package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerExecutionTest {

    @Test
    void handle() throws Exception {
        TestController controller = new TestController();

        HandlerExecution handlerExecution = new HandlerExecution(controller, controller.getClass().getMethod("test", HttpServletRequest.class, HttpServletResponse.class));

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("hello")).isEqualTo("world");
    }

    @Test
    void handle_request() throws Exception {
        TestController controller = new TestController();

        HandlerExecution handlerExecution = new HandlerExecution(controller, controller.getClass().getMethod("testRequest", HttpServletRequest.class, HttpServletResponse.class));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("hello", "world");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("hello")).isEqualTo("world");
    }

    static class TestController {
        public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("hello", "world");

            return modelAndView;
        }

        public ModelAndView testRequest(HttpServletRequest request, HttpServletResponse response) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("hello", request.getHeader("hello"));

            return modelAndView;
        }
    }
}
