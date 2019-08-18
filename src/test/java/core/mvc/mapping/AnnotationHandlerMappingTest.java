package core.mvc.mapping;

import core.mvc.handler.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc");
        handlerMapping.initialize();
    }

    @Test
    public void getHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/findUserId");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);

        assertThat(execution).isNotNull();
        execution.handle(request, response);
    }

    @DisplayName("requestURI 에 PathVariable 이 있을때")
    @Test
    public void getHandler1() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/{id}");
        HandlerExecution execution = handlerMapping.getHandler(request);

        assertThat(execution).isNotNull();
    }

    @DisplayName("매핑 되지 않은 uri 일 때")
    @Test
    public void getHandler2() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        HandlerExecution execution = handlerMapping.getHandler(request);

        assertThat(execution).isNull();
    }
}
