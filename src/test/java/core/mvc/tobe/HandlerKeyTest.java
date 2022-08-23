package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerKeyTest {

    @DisplayName("pathvariable이 적용되어있는 Handler 중 해당하는 handler가 맞는지 판단한다.")
    @Test
    void matchedHandler() {
        String requestURI = "/users/1";
        HandlerKey handlerKey = new HandlerKey("/users/{id}", RequestMethod.GET);
        assertThat(handlerKey.matchedHandler(requestURI)).isTrue();
    }

    @DisplayName("pathvariable이 적용되어있지만 url 길이가 다르면 해당 URI가 아니다.")
    @Test
    void matchedHandler_notMatchedPathLength() {
        String requestURI = "/users/1";
        HandlerKey handlerKey = new HandlerKey("/users/{id}/test", RequestMethod.GET);
        assertThat(handlerKey.matchedHandler(requestURI)).isFalse();
    }

    @DisplayName("pathvariable이 적용되어있지 않으면 해당 URI가 아니다.")
    @Test
    void matchedHandler_notUsePathVariable() {
        String requestURI = "/users/1";
        HandlerKey handlerKey = new HandlerKey("/users/test", RequestMethod.GET);
        assertThat(handlerKey.matchedHandler(requestURI)).isFalse();
    }
}