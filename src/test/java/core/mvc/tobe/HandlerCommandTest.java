package core.mvc.tobe;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HandlerCommandTest {

    private RequestHandlerMapping mapping;
    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeAll
    void initData() {
        request = new MockHttpServletRequest("GET", "/users");
        response = new MockHttpServletResponse();
    }

//    @Test
//    public void requestHandlerCommand() throws Exception {
//        mapping = new RequestMapping();
//        mapping.initialize();
//
//        HandlerCommand handlerCommand = new RequestHandlerCommand(request, response);
//        Object handler = mapping.getHandler(request);
//        ModelAndView modelAndView = handlerCommand.execute(handler);
//
//        assertThat(modelAndView).isNotEqualTo(null);
//    }
//
//
//    @Test
//    public void annotationHandlerCommand() throws Exception {
//        mapping = new AnnotationHandlerMapping("core.mvc.tobe");
//        mapping.initialize();
//
//        HandlerCommand handlerCommand = new AnnotationHandlerCommand(request, response);
//        Object handler = mapping.getHandler(request);
//        ModelAndView modelAndView = handlerCommand.execute(handler);
//
//        assertThat(modelAndView).isEqualTo(null);
//    }
}
