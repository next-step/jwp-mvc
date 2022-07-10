package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class JavaBeanMethodArgumentResolverTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @DisplayName("Java Bean Type 파라미터 맵핑이 성공한다.")
    @Test
    void javaBean() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/javabean");
        request.setParameter("userId", "dean");
        request.setParameter("password", "password");
        request.setParameter("age", "30");

        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView handle = execution.handle(request, response);

        TestUser testUser = (TestUser) handle.getModel().get("testUser");

        assertThat(testUser.getUserId()).isEqualTo("dean");
        assertThat(testUser.getPassword()).isEqualTo("password");
        assertThat(testUser.getAge()).isEqualTo(30);
    }


}
