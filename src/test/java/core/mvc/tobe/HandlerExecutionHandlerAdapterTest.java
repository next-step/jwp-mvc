package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


class HandlerExecutionHandlerAdapterTest {

    private HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter;

    @BeforeEach
    void setup() {
        handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
    }

    @DisplayName("HandlerExecution 타입의 핸들러를 처리할 수 있다.")
    @Test
    void isSupportTest() throws Exception {
        // given
        MockController mockController = new MockController("");
        Class<? extends MockController> clazz = mockController.getClass();
        HandlerExecution handlerExecution = new HandlerExecution(mockController, clazz.getDeclaredMethod("execute", HttpServletRequest.class, HttpServletResponse.class));

        // when // then
        Assertions.assertThat(handlerExecutionHandlerAdapter.isSupport(handlerExecution))
                .isTrue();
    }

    @DisplayName("핸들러를 실행하면 Controller 의 execute 가 실행된다.")
    @Test
    void handleTest() throws Exception {
        // given
        MockController controller = new MockController("test.jsp");
        Class<? extends MockController> clazz = controller.getClass();
        HandlerExecution handlerExecution = new HandlerExecution(controller, clazz.getDeclaredMethod("execute", HttpServletRequest.class, HttpServletResponse.class));

        // when
        ModelAndView modelAndView = handlerExecutionHandlerAdapter.handle(handlerExecution, new MockHttpServletRequest(), new MockHttpServletResponse());

        // then
        Assertions.assertThat(modelAndView.getViewName()).isEqualTo("test.jsp");
    }

    private static class MockController {

        private final String viewName;

        private MockController(String viewName) {
            this.viewName = viewName;
        }

        ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
            return new ModelAndView(viewName);
        }

    }

}
