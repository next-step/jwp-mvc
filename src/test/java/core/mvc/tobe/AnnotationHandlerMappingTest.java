package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class AnnotationHandlerMappingTest {

  private AnnotationHandlerMapping handlerMapping;

  @BeforeEach
  public void setup() {
    handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
    handlerMapping.initialize();
  }

  @Test
  public void getHandler() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/show");
    MockHttpServletResponse response = new MockHttpServletResponse();
    HandlerExecution execution = handlerMapping.getHandler(request);
    execution.handle(request, response);
  }

  @Test
  @DisplayName("GET 메소드 지원 핸들러를 가지고온다")
  public void GET_Method_getHandler() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/show");
    MockHttpServletResponse response = new MockHttpServletResponse();
    HandlerExecution execution = handlerMapping.getHandler(request);
    execution.handle(request, response);
  }

  @Test
  @DisplayName("POST 메소드 지원 핸들러를 가지고온다")
  public void POST_Method_getHandler() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/create");
    MockHttpServletResponse response = new MockHttpServletResponse();
    HandlerExecution execution = handlerMapping.getHandler(request);
    execution.handle(request, response);
  }

  @ParameterizedTest
  @ValueSource(strings = {"POST", "GET"})
  @DisplayName("METHOD 가 지정되지 않은 핸들러는 GET POST 모두 지원한다")
  public void NO_Method_Mapping_getHandler(String method) throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest(method, "/users");
    MockHttpServletResponse response = new MockHttpServletResponse();
    HandlerExecution execution = handlerMapping.getHandler(request);
    execution.handle(request, response);

  }
}
