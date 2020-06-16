package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("Handler 와 매칭하기 위한 key 클래스")
class HandlerKeyTest {

    @ParameterizedTest
    @MethodSource
    @DisplayName("스태틱 팩토리 메서드 from")
    void from(final String requestUri, final String method, final HandlerKey expected) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(requestUri);
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

    @ParameterizedTest
    @MethodSource
    @DisplayName("생성자 호출시 url 혹은 request method 가 null 인 경우 예외 발생")
    void constructFail(final String url, final RequestMethod requestMethod) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new HandlerKey(url, requestMethod));
    }

    private static Stream<Arguments> constructFail() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, RequestMethod.GET),
                Arguments.of("url", null)
        );
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("HttpRequest 가 null 인 경우에 예외 발생")
    void fromStaticFactoryMethodThrowException(final HttpServletRequest request) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> HandlerKey.from(request));
    }

    @Test
    @DisplayName("동치성 테스트")
    void equals() {
        assertThat(new HandlerKey("/url", RequestMethod.GET))
                .isEqualTo(new HandlerKey("/url", RequestMethod.GET));
    }

    @Test
    @DisplayName("패턴을 적용했을때도 동일한지")
    void equalsUsingPathPattern() {
        HandlerKey origin = new HandlerKey("/user/{userId}", RequestMethod.GET);
        HandlerKey request = new HandlerKey("/user/1", RequestMethod.GET);

        assertThat(origin)
                .isEqualTo(request);
    }

    @Test
    @DisplayName("패턴을 셋에 적용했을때도 동일한지")
    void equalsWhenUsingSet() {
        HandlerKey origin = new HandlerKey("/user/{userId}", RequestMethod.GET);
        HandlerKey request = new HandlerKey("/user/1", RequestMethod.GET);

        Set<HandlerKey> keys = new HashSet<>();
        keys.add(origin);

        assertThat(keys.contains(request)).isTrue();
    }

    @Test
    @DisplayName("패턴을 맵에 적용했을때도 동일한지")
    void equalsWhenUsingMap() {
        HandlerKey origin = new HandlerKey("/user/{userId}", RequestMethod.GET);
        HandlerKey request = new HandlerKey("/user/1", RequestMethod.GET);

        Map<HandlerKey, Object> keyMap = new HashMap();
        keyMap.put(origin, origin);

        assertThat(keyMap.containsKey(request)).isTrue();
    }
}