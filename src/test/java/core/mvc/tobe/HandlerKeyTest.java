package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Handler 와 매칭하기 위한 key 클래스")
class HandlerKeyTest {

    @ParameterizedTest
    @MethodSource
    @DisplayName("스태틱 팩토리 메서드 from")
    void from(final String path, final String method, final HandlerKey expected) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setPathInfo(path);
        request.setMethod(method);

        HandlerKey key = HandlerKey.from(request);

        assertThat(key).isEqualTo(expected);
    }

    private static Stream<Arguments> from() {
        String path = "/hello-world";
        RequestMethod[] requestMethods = RequestMethod.values();

        return Stream.of(Arrays.stream(requestMethods)
                .map(requestMethod ->
                        Arguments.of(
                                path,
                                requestMethod.name(),
                                new HandlerKey(path, requestMethod)
                        )
                ).toArray(Arguments[]::new)
        );
    }
}