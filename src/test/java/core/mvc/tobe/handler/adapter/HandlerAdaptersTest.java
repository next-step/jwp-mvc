package core.mvc.tobe.handler.adapter;

import core.mvc.asis.Controller;
import core.mvc.tobe.view.ModelAndView;
import core.mvc.tobe.view.SimpleNameView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerAdaptersTest {

    @DisplayName("핸들러를 실행시킬수 있는 어댑터를 들고있는 경우, 해당 어댑터를 통해 핸들러를 실행하고 ModelAndView 타입을 반환.")
    @Test
    void handle() {
        HandlerAdapters handlerAdapters = new HandlerAdapters(
                List.of(
                        new ControllerHandlerAdapter()
                )
        );

        ModelAndView actual = handlerAdapters.handle(
                new MockController(),
                new MockHttpServletRequest(),
                new MockHttpServletResponse()
        );

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(
                        new ModelAndView(
                                new SimpleNameView("test")
                        )
                );
    }

    @DisplayName("핸들러를 실행시킬수 있는 어댑터가 없는 경우, 예외 발생")
    @Test
    void handle_fail() {
        HandlerAdapters handlerAdapters = new HandlerAdapters(
                List.of()
        );

        assertThatThrownBy(() ->
                handlerAdapters.handle(
                        new MockController(),
                        new MockHttpServletRequest(),
                        new MockHttpServletResponse()
                )
        ).isInstanceOf(NoExistsAdapterException.class)
                .hasMessage("요청을 처리하기 위해 핸들러를 실행할 어댑터가 존재하지 않습니다.");
    }

    private static class MockController implements Controller {
        @Override
        public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
            return "test";
        }
    }
}