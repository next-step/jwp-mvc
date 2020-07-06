package core.mvc.tobe.dispatcherservlet;

import core.mvc.ModelAndView;
import core.mvc.asis.DispatcherServlet;
import core.mvc.tobe.handlermapping.HandlerMapping;
import core.mvc.tobe.handlermapping.HandlerMappings;
import core.mvc.tobe.view.JspView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DispatcherServletTest {
    private final DispatcherServlet DISPATCH_SERVLET = new DispatcherServlet();

    @BeforeEach
    void setUp() {
        HandlerMappings.clear();
        DISPATCH_SERVLET.init();
    }

    @DisplayName("초기화가 되면, 각각의 HandlerMapping은 null이 아님")
    @Test
    void init() {
        Set<HandlerMapping> handlerMappings = HandlerMappings.getHandlerMappings();
        assertThat(handlerMappings.size()).isEqualTo(2);
    }

    @DisplayName("초기화가 되면 컨트롤러는 적절한 ModelAndView를 생성한다. - AnnotationHandlerMapping")
    @Test
    void getModelAndViewForUrlAnnotationHandlerMapping() throws InstantiationException {
        //given
        MockHttpServletRequest request
                = new MockHttpServletRequest("POST", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        ModelAndView modelAndView = DISPATCH_SERVLET.getModelAndView(request, response);

        //then
        assertThat(modelAndView.getModel()).hasFieldOrProperty("user");
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }

    @DisplayName("초기화가 되면 컨트롤러는 적절한 ModelAndView를 생성한다. - UrlHandlerMapping")
    @Test
    void getModelAndViewForUrlHandlerMapping() throws InstantiationException {
        //given
        MockHttpServletRequest request
                = new MockHttpServletRequest(null, "/users/create");
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        ModelAndView modelAndView = DISPATCH_SERVLET.getModelAndView(request, response);

        //then
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
