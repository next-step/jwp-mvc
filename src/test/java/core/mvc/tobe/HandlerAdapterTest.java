package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import core.mvc.ControllerHandlerAdapter;
import core.mvc.ExecutionHandlerAdapter;
import core.mvc.HandlerAdapter;
import core.mvc.asis.Controller;
import next.controller.LoginController;

public class HandlerAdapterTest {

	private static final HandlerAdapter<?> CONTROLLER_ADAPTER = new ControllerHandlerAdapter();
	private static final HandlerAdapter<?> EXECUTION_ADAPTER = new ExecutionHandlerAdapter();
	private static final Controller LOGIN_CONTROLLER = new LoginController();
	private static final String HTTP_METHOD = "GET";
	private static final String REQUEST_URI = "/users/findUserId";
	private static final String BASE_PACKAGE = "core.mvc.tobe";
    private static final AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);

    static {
    	handlerMapping.initialize();
    }

    @DisplayName("ExecutionHandler handlerAdapter 테스트")
    @Test
    public void getHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI);
        HandlerExecution execution = handlerMapping.getHandler(request);
        assertThat(CONTROLLER_ADAPTER.supports(execution))
		.isFalse();
		assertThat(EXECUTION_ADAPTER.supports(execution))
		.isTrue();
    }
    
    @DisplayName("Controller handlerAdapter 테스트")
	@Test
	public void getHandlerModelAndView() throws Exception {
		assertThat(CONTROLLER_ADAPTER.supports(LOGIN_CONTROLLER))
		.isTrue();
		assertThat(EXECUTION_ADAPTER.supports(LOGIN_CONTROLLER))
		.isFalse();
	}
}
