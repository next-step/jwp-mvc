package core.mvc.tobe;

import core.mvc.asis.DispatcherServlet;
import core.mvc.tobe.handlermapping.HandlerMappings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class DispatcherServletTest {
    @DisplayName("init() 메소드 호출 - UrlMappingHandler (handler 존재)")
    @ParameterizedTest
    @ValueSource(strings = {"/users", "/users/login", "/users/loginForm"})
    void initForUrlHandlerMapping(String url) throws ServletException, IOException {
        //given
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        MockHttpServletRequest request = new MockHttpServletRequest(null, url);

        //when
        dispatcherServlet.init();

        //then
        boolean hasMatchedHandler = HandlerMappings.getHandlerMappings()
                .stream()
                .filter(handlerMapping -> handlerMapping.hasHandler(request))
                .findAny()
                .isPresent();
        assertThat(hasMatchedHandler).isTrue();
    }

    @DisplayName("init() 메소드 호출 - UrlMappingHandler (handler 존재 X)")
    @ParameterizedTest
    @ValueSource(strings = {"/users/search", "/member/login"})
    void initForUrlHandlerMappingWhenNotMatched(String url) throws ServletException, IOException {
        //given
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        MockHttpServletRequest request = new MockHttpServletRequest(null, url);

        //when
        dispatcherServlet.init();

        //then
        boolean hasMatchedHandler = HandlerMappings.getHandlerMappings()
                .stream()
                .filter(handlerMapping -> handlerMapping.hasHandler(request))
                .findAny()
                .isPresent();
        assertThat(hasMatchedHandler).isFalse();
    }

    @DisplayName("init() 메소드 호출 - AnnotationMappingHandler (handler 존재)")
    @ParameterizedTest
    @CsvSource(value = {"GET:/users/show", "POST:/users"}, delimiter = ':')
    void initForAnnotationHandlerMapping(String method, String url) throws ServletException, IOException {
        //given
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        MockHttpServletRequest request = new MockHttpServletRequest(method, url);

        //when
        dispatcherServlet.init();

        //then
        boolean hasMatchedHandler = HandlerMappings.getHandlerMappings()
                .stream()
                .filter(handlerMapping -> handlerMapping.hasHandler(request))
                .findAny()
                .isPresent();
        assertThat(hasMatchedHandler).isTrue();
    }

    @DisplayName("init() 메소드 호출 - AnnotationMappingHandler (handler 존재 X)")
    @ParameterizedTest
    @CsvSource(value = {"GET:/users/search", "POST:/members"}, delimiter = ':')
    void initForAnnotationHandlerMappingWhenNotMatched(String method, String url) throws ServletException, IOException {
        //given
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        MockHttpServletRequest request = new MockHttpServletRequest(method, url);

        //when
        dispatcherServlet.init();

        //then
        boolean hasMatchedHandler = HandlerMappings.getHandlerMappings()
                .stream()
                .filter(handlerMapping -> handlerMapping.hasHandler(request))
                .findAny()
                .isPresent();
        assertThat(hasMatchedHandler).isFalse();
    }
}
