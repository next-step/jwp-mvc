package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;

class HandlerKeyTest {

    @ParameterizedTest
    @MethodSource("provideHandlerKeys")
    void match(HandlerKey input, boolean expected) {
        HandlerKey handlerKey = new HandlerKey("/users", RequestMethod.GET);
        assertThat(handlerKey.equals(input)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideHandlerKeys() {
        return Stream.of(
                Arguments.of(new HandlerKey("/users", RequestMethod.GET), true),
                Arguments.of(new HandlerKey("/users", RequestMethod.POST), false),
                Arguments.of(new HandlerKey("/users/show", RequestMethod.GET), false),
                Arguments.of(new HandlerKey("/users/1", RequestMethod.GET), false)
        );
    }

}