package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.ModelAndViewHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationHandlerMappingTest {
	private static final String BASE_PACKAGE = "core.mvc.tobe";
	private static final String HTTP_METHOD = "GET";
	private static final String REQUEST_URI = "/users/findUserId";
	
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        handlerMapping.initialize();
    }

    @DisplayName("handler 호출 테스트")
    @Test
    public void getHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI);
        MockHttpServletResponse response = new MockHttpServletResponse();
        ModelAndViewHandler execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }
    
    @DisplayName("handler 호출 테스트 : return model 비교")
    @Test
    public void getHandlerModelAndView() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(HTTP_METHOD, REQUEST_URI);
        MockHttpServletResponse response = new MockHttpServletResponse();
        ModelAndViewHandler execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);
        assertThat(modelAndView).isNotNull();
        
        Map<String, Object> model = modelAndView.getModel();
        assertThat(model).isNotNull();
        assertThat(model.get(MyController.ATTRIBUTE_USER_ID_NAME)).isEqualTo(MyController.ATTRIBUTE_USER_ID_VALUE);
    }
}
