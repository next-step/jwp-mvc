package next.controller;

import core.mvc.ModelAndView;
import core.mvc.asis.DispatcherServlet;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HomeController AnnotationMapping Test")
class HomeControllerTest {

    private AnnotationHandlerMapping handlerMapping;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("next.controller");
        handlerMapping.initialize();

        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @Test
    @DisplayName("Annotation Mapping 으로 API 요청을 처리한다.")
    void index() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView modelAndView = execution.handle(request, response);
        Map<String, Object> model = modelAndView.getModel();

        assertThat(model).hasSize(1);
        assertThat(model.containsKey("users")).isTrue();
    }

    @Test
    @DisplayName("DispatcherServlet 으로 API 요청을 처리한다.")
    void dispatcher_service() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);
    }
}
