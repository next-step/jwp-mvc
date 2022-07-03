package core.mvc;

import core.mvc.asis.LegacyHandlerMapping;
import core.mvc.tobe.HandlerMappings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerMappingsTest {

    @DisplayName("HandlerMapping이 존재하지 않을 경우 예외를 반환 할 수 있다")
    @Test
    void noSuchException() {
        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(new LegacyHandlerMapping());

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setRequestURI("/users");

        assertThatThrownBy(() -> handlerMappings.getHandler(mockHttpServletRequest))
                .isInstanceOf(NoSuchElementException.class);
    }
}
