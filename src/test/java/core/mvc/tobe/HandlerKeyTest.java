package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerKeyTest {

    @Test
    @DisplayName("url 매치 테스트")
    void equals() {
        HandlerKey handlerKey = new HandlerKey("/users/profile/{id}", RequestMethod.GET);
        HandlerKey requestHandlerKey = new HandlerKey("/users/profile/ninjasul", RequestMethod.GET);

        assertThat(handlerKey.equals(requestHandlerKey)).isTrue();
    }

    @Test
    @DisplayName("url 매치 테스트")
    void notEquals() {
        HandlerKey handlerKey = new HandlerKey("/users/profile/{id}", RequestMethod.GET);
        HandlerKey requestHandlerKey = new HandlerKey("/users/profile", RequestMethod.GET);

        assertThat(handlerKey.equals(requestHandlerKey)).isFalse();
    }

}