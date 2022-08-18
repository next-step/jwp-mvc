package core.mvc.tobe;

import core.mvc.asis.RequestMapping;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerMappingsTest {

    @Test
    void create() {
        HandlerMappings handlerMappings = new HandlerMappings(new AnnotationHandlerMapping(), new RequestMapping());

        assertThat(handlerMappings.getMappings()).hasSize(2);
    }
}
