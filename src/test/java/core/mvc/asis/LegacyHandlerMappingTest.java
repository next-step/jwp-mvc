package core.mvc.asis;

import core.mvc.HandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class LegacyHandlerMappingTest {
    private HandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new LegacyHandlerMapping();
    }

    @DisplayName("controller 기본 테스트")
    @Test
    public void getHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Controller controller = (Controller) handlerMapping.getHandler(request);
        controller.execute(request, response);
    }

    @DisplayName("forward 테스트")
    @Test
    public void getHandlerMultiple() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/loginForm");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Controller controller = (Controller) handlerMapping.getHandler(request);
        controller.execute(request, response);
    }
}
