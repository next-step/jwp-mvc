package core.mvc.tobe;

import core.mvc.asis.DispatcherServlet;
import core.mvc.tobe.handlermapping.HandlerMapping;
import core.mvc.tobe.handlermapping.HandlerMappings;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DispatcherServletTest {
    @DisplayName("초기화가 되면, 각각의 HandlerMapping은 null이 아님")
    @Test
    void init() {
        //given
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        //when
        dispatcherServlet.init();

        //then
        List<HandlerMapping> handlerMappings = HandlerMappings.getHandlerMappings();
        assertThat(handlerMappings).hasSize(2);
    }
}
