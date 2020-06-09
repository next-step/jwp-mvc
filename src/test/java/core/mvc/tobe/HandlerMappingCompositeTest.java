package core.mvc.tobe;

import core.mvc.asis.Controller;
import core.mvc.asis.RequestMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author KingCjy
 */
public class HandlerMappingCompositeTest {

    private HandlerMappingComposite handlerMappingComposite;

    @BeforeEach
    public void init() {
        RequestMapping requestMapping = new RequestMapping();
        requestMapping.initMapping();

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        annotationHandlerMapping.initialize();

        handlerMappingComposite = new HandlerMappingComposite(requestMapping, annotationHandlerMapping);
    }

    @Test
    @DisplayName("기존 레거시 RequestMapping 사용")
    public void getHandlerFromRequestMapping() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");

        Object handler = handlerMappingComposite.getHandler(request);

        assertThat(handler).isInstanceOf(Controller.class);
    }

    @Test
    @DisplayName("신규 AnnotationHandlerMapping 사용")
    public void getHandlerFromAnnotationHandlerMapping() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/test");

        Object handler = handlerMappingComposite.getHandler(request);

        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
