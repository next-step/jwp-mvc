package core.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        Environment.RESOURCE_NAME = "config-test.properties";
        dispatcherServlet = new StubDispatcherServlet();
        dispatcherServlet.init();
    }

    @DisplayName("dispatcher servlet service process")
    @ParameterizedTest(name = "call {0} -> result: {2}")
    @MethodSource("sampleReqResp")
    public void dispatcherServlet(HttpServletRequest request, HttpServletResponse response, String expected) throws ServletException {
        dispatcherServlet.service(request, response);
        assertThat(request.getAttribute("mock")).isEqualTo(expected);
    }

    private static Stream<Arguments> sampleReqResp() {
        return Stream.of(
                Arguments.of(new MockHttpServletRequest("GET", "/mock/users"), new MockHttpServletResponse(), "users"),
                Arguments.of(new MockHttpServletRequest("POST", "/mock/qna"), new MockHttpServletResponse(), "qna")
        );
    }

    @DisplayName("dispatcher string service process")
    @Test
    public void dispatcherServletWithString() throws ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/mock/string");
        request.setParameter("string", "ok");
        HttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);
        assertThat(request.getAttribute("mock")).isEqualTo("ok");
    }

}
