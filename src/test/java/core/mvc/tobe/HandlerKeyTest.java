package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class HandlerKeyTest {

    @DisplayName("HandlerKey의 path pattern과 request method가 같으면 HandlerKey는 동등하다")
    @ParameterizedTest
    @CsvSource(value = {
        "/users/{id}, /users/{id}",
        "/users/{id}, /users/1",
        "/users, /users"
    })
    void match_path_pattern_url(final String first, final String second) {
        final HandlerKey actual = new HandlerKey(first, RequestMethod.GET);

        final HandlerKey expected = new HandlerKey(second, RequestMethod.GET);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("HandlerKey의 path pattern이 다르고 request method가 같으면 HandlerKey는 동등하지 않다")
    @ParameterizedTest
    @CsvSource(value = {
        "/users, /users/1",
        "/users/{id}, /users"
    })
    void not_match_path_pattern_url(final String first, final String second) {
        final HandlerKey actual = new HandlerKey(first, RequestMethod.GET);

        final HandlerKey expected = new HandlerKey(second, RequestMethod.GET);

        assertThat(actual).isNotEqualTo(expected);
    }

    @DisplayName("HandlerKey의 path pattern이 동일해도 request method가 다르면 HandlerKey는 동등하지 않다")
    @Test
    void not_match_request_method() {
        final HandlerKey actual = new HandlerKey("/users", RequestMethod.GET);

        final HandlerKey expected = new HandlerKey("/users", RequestMethod.POST);

        assertThat(actual).isNotEqualTo(expected);
    }
}
