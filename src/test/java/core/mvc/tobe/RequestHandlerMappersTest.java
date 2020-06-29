package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import core.mvc.asis.RequestMapping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RequestHandlerMappersTest {

    MockHttpServletRequest request;
    MockHttpServletResponse response;
    RequestHandlerMappers handlerMappers;

    @BeforeAll
    void initData() {
        response = new MockHttpServletResponse();

        handlerMappers = new RequestHandlerMappers();
        handlerMappers.addMapper(new RequestMapping());
        handlerMappers.addMapper(new AnnotationHandlerMapping("core.mvc.tobe"));
    }

    @Test
    public void mapperHandlingRequestHandler() throws Exception {
        request = new MockHttpServletRequest("GET", "/users");
        ModelAndView modelAndView = handlerMappers.mapperHandling(request, response);

        assertThat(modelAndView).isNotEqualTo(null);
        assertThat(modelAndView.getView()).isEqualToComparingFieldByField(new RedirectView("redirect:/users/loginForm"));
    }

    @Test
    public void mapperHandlingAnnotationHandler() throws Exception {
        request = new MockHttpServletRequest("GET", "/users/test");
        ModelAndView modelAndView = handlerMappers.mapperHandling(request, response);

        assertThat(modelAndView).isEqualTo(null);
    }
}
