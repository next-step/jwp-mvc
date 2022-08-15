package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class ControllerHandlerAdapterTest {

    private ControllerHandlerAdapter controllerHandlerAdapter;

    @BeforeEach
    void setup() {
        controllerHandlerAdapter = new ControllerHandlerAdapter();
    }

    @DisplayName("Contoller 타입의 핸들러를 처리할 수 있다.")
    @Test
    void isSupportTest() {
        // given
        MockController controller = new MockController("");
        NotController notController = new NotController();

        // when // then
        Assertions.assertThat(controllerHandlerAdapter.isSupport(controller))
                .isTrue();

        Assertions.assertThat(controllerHandlerAdapter.isSupport(notController))
                .isFalse();
    }

    @DisplayName("핸들러를 실행하면 Controller 의 execute 가 실행된다.")
    @Test
    void handleTest() throws Exception {
        // given
        MockController controller = new MockController("test.jsp");

        // when
        ModelAndView modelAndView = controllerHandlerAdapter.handle(controller, new MockHttpServletRequest(), new MockHttpServletResponse());

        // then
        Assertions.assertThat(modelAndView.getViewName()).isEqualTo("test.jsp");
    }

    private static class MockController implements Controller {

        private final String viewName;

        public MockController(String viewName) {
            this.viewName = viewName;
        }

        @Override
        public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
            return viewName;
        }
    }

    private static class NotController {

    }

}
