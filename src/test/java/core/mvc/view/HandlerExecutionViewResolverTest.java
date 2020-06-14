package core.mvc.view;


import core.mvc.asis.Controller;
import next.controller.HomeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Handler execution 을 수행하고 그 결과를 model and view 로 처리해서 리턴해주는 리졸버")
class HandlerExecutionViewResolverTest {
    private static final HandlerExecutionViewResolver handlerExecutionViewResolver =
            new HandlerExecutionViewResolver();

    @Test
    @DisplayName("핸들러가 HandlerExecution 이 아닐경우 null 을 리턴")
    void handle() throws Exception {
        Controller controller = new HomeController();
        assertThat(
                handlerExecutionViewResolver.handle(
                        controller,
                        new MockHttpServletRequest(),
                        new MockHttpServletResponse()
                )
        ).isNull();
    }

}