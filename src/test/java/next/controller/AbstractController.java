package next.controller;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Ignore
public class AbstractController {

    public static final String NEXT_CONTROLLER = "next.controller";
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() throws Exception {
        handlerMapping = new AnnotationHandlerMapping(NEXT_CONTROLLER);
        handlerMapping.initialize();
    }

    protected ModelAndView execute(HttpServletRequest request) throws Exception {
        return execute(request, new MockHttpServletResponse());
    }

    protected ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecution execution = handlerMapping.getHandler(request);
        return execution.handle(request, response);
    }

    protected ModelAndView execute(String method, String url) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, url);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        return execution.handle(request, response);
    }
}

