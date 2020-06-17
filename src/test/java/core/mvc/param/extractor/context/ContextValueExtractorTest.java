package core.mvc.param.extractor.context;

import core.mvc.param.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요청에 대한 컨텍스트를 추출하기 위한 클래스")
class ContextValueExtractorTest {
    private static final ContextValueExtractor CONTEXT_VALUE_EXTRACTOR = new ContextValueExtractor();

    @ParameterizedTest
    @MethodSource
    @DisplayName("requset, response, http session 을 추출할 수 있다")
    void extract(final Parameter parameter, final HttpServletRequest request) {
        Object value = CONTEXT_VALUE_EXTRACTOR.extract(parameter, request);

        assertThat(value).isNotNull();
    }

    private static Stream<Arguments> extract() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Map<Class<?>, Object> context = new HashMap<>();
        context.put(HttpServletRequest.class, request);
        context.put(HttpServletResponse.class, new MockHttpServletResponse());
        context.put(HttpSession.class, new MockHttpSession());

        request.setAttribute(ContextValueExtractor.CONTEXTS, context);

        return Stream.of(
                Arguments.of(new Parameter("request", HttpServletRequest.class, null), request),
                Arguments.of(new Parameter("response", HttpServletResponse.class, null), request),
                Arguments.of(new Parameter("session", HttpSession.class, null), request)
        );
    }

}