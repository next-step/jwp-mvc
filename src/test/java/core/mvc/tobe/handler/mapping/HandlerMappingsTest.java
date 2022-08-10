package core.mvc.tobe.handler.mapping;

import core.mvc.asis.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerMappingsTest {

    @DisplayName("해당 객체가 들고있는 핸들러 매핑들을 순회하면서, 요청을 처리할수 있는 핸들러를 반환한다.")
    @Test
    void getHandler() {
        HandlerMappings handlerMappings = new HandlerMappings(
                List.of(
                        new NeverUsableHandlerMapping(),
                        new AlwaysUsableHandlerMapping()
                )
        );

        Object actual = handlerMappings.getHandler(new MockHttpServletRequest("GET", "/user"));
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(new TestImplementController());

    }

    @DisplayName("해당 객체가 요청을 처리할 수 있는 핸들러를 반환하는 핸들러 매핑이 존재하지 않으면, 예외발생")
    @Test
    void getHandler_fail() {
        HandlerMappings handlerMappings = new HandlerMappings(
                List.of(
                        new NeverUsableHandlerMapping()
                )
        );

        assertThatThrownBy(() -> handlerMappings.getHandler(new MockHttpServletRequest("GET", "/user")))
                .isInstanceOf(NoExistsHandlerException.class)
                .hasMessage("요청을 처리할 핸들러가 존재하지 않습니다.");
    }

    private static class NeverUsableHandlerMapping implements HandlerMapping {
        @Override
        public Object getHandler(HttpServletRequest request) {
            return null;
        }
    }

    private static class AlwaysUsableHandlerMapping implements HandlerMapping {
        @Override
        public Object getHandler(HttpServletRequest request) {
            return new TestImplementController();
        }
    }

    private static class TestImplementController implements Controller {
        @Override
        public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
            return "test";
        }
    }
}