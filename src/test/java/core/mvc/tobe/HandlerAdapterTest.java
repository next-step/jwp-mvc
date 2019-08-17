package core.mvc.tobe;

import core.mvc.asis.Controller;
import next.controller.HomeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerAdapterTest {

    @DisplayName("support - legacyHandlerAdapter")
    @Test
    void supportLegacyHandlerAdapter() {
        //givne
        HandlerAdapter handlerAdapter = new LegacyHandlerAdapter();
        Controller controller = new HomeController();

        //when, then
        assertThat(handlerAdapter.support(controller)).isTrue();
    }

    @DisplayName("support - annotationHandlerAdapter")
    @Test
    void supportAnnotationHandlerAdapter() {
        //givne
        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        HandlerExecution handlerExecution = new HandlerExecution(null, null);

        //when, then
        assertThat(handlerAdapter.support(handlerExecution)).isTrue();
    }
}