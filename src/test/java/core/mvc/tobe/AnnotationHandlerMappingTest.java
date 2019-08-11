package core.mvc.tobe;

import core.mvc.JSPView;
import core.mvc.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() throws Exception {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @DisplayName("RequestMethod가 존재하지 않을 시 모든 메소드에 매핑된다")
    @Test
    public void getHandler_Default() throws Exception {
        ModelAndView handle = execute("GET", "/users/findUserId");

        assertThat(handle.getView()).isEqualTo(new JSPView("/user/show.jsp"));
    }

    @DisplayName("RequestMethod GET 요청에 매핑을 성공한다")
    @Test
    public void getHandler_GET() throws Exception {
        ModelAndView handle = execute("GET", "/users");

        assertThat(handle.getView()).isEqualTo(new JSPView("/users/list.jsp"));
    }

    @DisplayName("RequestMethod POST 요청에 매핑을 성공한다")
    @Test
    public void getHandler_POST() throws Exception {
        ModelAndView handle = execute("POST", "/users");

        assertThat(handle.getView()).isEqualTo(new JSPView("redirect:/users"));
    }

    private ModelAndView execute(String method, String url) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, url);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        return execution.handle(request, response);
    }

    @DisplayName("매핑되는 핸들러가 없을 경우 null을 반환한다")
    @Test
    public void getHandler_Exception() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/none");

        HandlerExecution execution = handlerMapping.getHandler(request);

        assertThat(execution).isNull();
    }
}