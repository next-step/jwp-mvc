package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author KingCjy
 */
public class HandlerExecutorCompositeTest {

    private HandlerExecutor handlerExecutor;

    @BeforeEach
    public void before() {
        handlerExecutor = new HandlerExecutorComposite(
                new ControllerExecutor(),
                new AnnotationHandlerExecutor()
        );
    }

    @Test
    @DisplayName("Controller execute test")
    public void executeController() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();

        AnnotationHandler annotationHandler = new AnnotationHandler(new MyController(), MyController.class.getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class));

        assertThat(handlerExecutor.supportHandler(annotationHandler)).isTrue();
        assertThat(handlerExecutor.execute(request, response, annotationHandler)).isNull();
    }

    @Test
    @DisplayName("AnnotationHandler execute test")
    public void executeAnnotationHander() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test/users");
        MockHttpServletResponse response = new MockHttpServletResponse();

        AnnotationHandler annotationHandler = new AnnotationHandler(new TestAnnotationController(), TestAnnotationController.class.getDeclaredMethod("getUsers"));

        assertThat(handlerExecutor.supportHandler(annotationHandler)).isTrue();
        assertThat(handlerExecutor.execute(request, response, annotationHandler)).isNotNull();
    }
}
