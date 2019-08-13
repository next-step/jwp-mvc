package core.mvc;

import core.mvc.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HandlerAdapterTest {

    @DisplayName("해당 handler를 support하는지 테스트")
    @Test
    void handler_adapter_test() {
        AnnotationHandlerMapping mapping = new AnnotationHandlerMapping("next");
        mapping.initialize();

        HttpServletRequest request = new MockHttpServletRequest("GET", "/");;
        HandlerAdapter adapter = new AnnotationHandler();
        Object handler = mapping.getHandler(request);

        assertTrue(adapter.supports(handler));
    }
}