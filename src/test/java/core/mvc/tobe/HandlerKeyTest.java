package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("핸들러 키")
class HandlerKeyTest {

    @Test
    @DisplayName("url 과 메소드로 생성")
    void instance() {
        assertThatNoException().isThrownBy(() -> new HandlerKey("/users", RequestMethod.POST));
    }

    @Test
    @DisplayName("일치 여부")
    void matches() {
        //given
        HandlerKey userUrlPattern = new HandlerKey("/users/{id}", RequestMethod.GET);
        HandlerKey oneIdUserUrl = new HandlerKey("/users/1", RequestMethod.GET);
        //when
        assertThat(userUrlPattern.matches(oneIdUserUrl)).isTrue();
    }
}
