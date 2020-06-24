package core.mvc.tobe.controller;

import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UpdateFormUserControllerTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @Test
    void updateFormUser() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/updateForm");
        request.setParameter("userId", "seongju");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        assertThatExceptionOfType(InvocationTargetException.class).isThrownBy(() -> execution.handle(request, response));
    }
}
