package core.mvc.tobe.resolver;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.TestUser;

class SimpleHandlerMethodArgumentResolverTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        this.handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe.resolver");
        this.handlerMapping.initialize();
    }

    @Test
    void 파라미터_매핑_문자열() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.addParameter("userId", "1");
        request.addParameter("password", "test");

        MockHttpServletResponse response = new MockHttpServletResponse();
        var execution = handlerMapping.getHandler(request);
        ((HandlerExecution)execution).handle(request, response);

        var model = ((HandlerExecution)execution).handle(request, response)
            .getModel();

        assertThat(model.get("userId")).isEqualTo("1");
        assertThat(model.get("password")).isEqualTo("test");
    }

    @Test
    void 파라미터_매핑_int() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/v1/users");
        request.addParameter("id", "1");
        request.addParameter("age", "20");

        MockHttpServletResponse response = new MockHttpServletResponse();
        var execution = handlerMapping.getHandler(request);
        var model = ((HandlerExecution)execution).handle(request, response)
            .getModel();

        assertThat(model.get("id")).isEqualTo(1L);
        assertThat(model.get("age")).isEqualTo(20);
    }

    @Test
    void 파라미터_매핑_자바빈() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/v2/users");
        request.addParameter("userId", "testId");
        request.addParameter("password", "votmdnjem");
        request.addParameter("age", "20");


        MockHttpServletResponse response = new MockHttpServletResponse();
        var execution = handlerMapping.getHandler(request);

        var model = ((HandlerExecution)execution).handle(request, response)
            .getModel();
        var actual = (TestUser) model.get("testUser");

        assertThat(actual.getUserId()).isEqualTo("testId");
        assertThat(actual.getPassword()).isEqualTo("votmdnjem");
        assertThat(actual.getAge()).isEqualTo(20);
    }

    @Test
    void 파라미터_매핑_PATH_VARIABLE() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        var execution = handlerMapping.getHandler(request);

        var handle = ((HandlerExecution)execution).handle(request, response);

        var model = handle.getModel();

        assertThat(model.get("id")).isEqualTo(1L);
    }
}