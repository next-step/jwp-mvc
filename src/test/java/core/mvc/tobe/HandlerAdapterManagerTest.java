package core.mvc.tobe;

import core.mvc.Environment;
import core.mvc.asis.RequestMappingHandlerAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerAdapterManagerTest {

    @DisplayName("HandlerAdapterManager each adapter handling Test")
    @ParameterizedTest(name = "request: {0} -> adapter class: {1}")
    @MethodSource("sampleRequest")
    void handlerAdapterManager(HttpServletRequest request, Class<?> expectedClass) throws ServletException {
        Properties properties = new Properties();
        properties.setProperty("component.basepackage", "next.mock");

        Environment environment = new Environment();
        environment.setConfig(properties);

        HandlerAdapterManager manager = new HandlerAdapterManager(environment);
        final HandlerAdapter handler = manager.getHandler(request);

        assertThat(handler).isExactlyInstanceOf(expectedClass);
    }

    private static Stream<Arguments> sampleRequest() {
        return Stream.of(
                Arguments.of(new MockHttpServletRequest("POST", "/mock/qna"), AnnotationHandlerMappingAdapter.class),
                Arguments.of(new MockHttpServletRequest("GET", "/users/form"), RequestMappingHandlerAdapter.class)
        );
    }


}
