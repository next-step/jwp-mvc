package core.mvc.asis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RequestMappingHandlerAdapterTest {

    @DisplayName("RequestMappingHandlerAdapter uri mapping test")
    @ParameterizedTest(name = "request: {0}")
    @MethodSource("sampleRequest")
    void requestMappingHandlerAdapter(HttpServletRequest request) throws Exception {
        RequestMapping requestMapping = new RequestMapping();
        MockHttpServletResponse response = new MockHttpServletResponse();
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter(requestMapping);

        if (!handlerAdapter.supports(request)) {
            Assertions.fail();
        }

        assertNotNull(handlerAdapter.handle(request, response));
    }

    private static Stream<Arguments> sampleRequest() {
        return Stream.of(
                Arguments.of(new MockHttpServletRequest("GET", "/users/form")),
                Arguments.of(new MockHttpServletRequest("GET", "/users/loginForm"))
        );
    }

}
