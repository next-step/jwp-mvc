package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @Test
    void getHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/findUserId");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }

    @DisplayName("GET 매핑을 확인한다.")
    @Test
    void getHandlerGet() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/findUserId");

        // when
        final HandlerExecution execution = handlerMapping.getHandler(request);
        final Object controller = execution.getController();

        // then
        assertThat(controller.getClass()).isSameAs(MyController.class);
    }

    @DisplayName("POST 매핑을 확인한다.")
    @Test
    void getHandlerPost() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");

        // when
        final HandlerExecution execution = handlerMapping.getHandler(request);
        final Object controller = execution.getController();

        // then
        assertThat(controller.getClass()).isSameAs(MyController.class);
    }

    @DisplayName("매핑이 안되면 null을 반환한다.")
    @Test
    void getHandlerNotFound() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/not-found");

        // when
        final HandlerExecution execution = handlerMapping.getHandler(request);

        // then
        assertThat(execution).isNull();
    }
}
