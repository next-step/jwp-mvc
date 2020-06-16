package core.mvc.view;


import core.annotation.web.RequestMethod;
import core.mvc.asis.Controller;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerKey;
import next.controller.HomeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Handler execution 을 수행하고 그 결과를 model and view 로 처리해서 리턴해주는 리졸버")
class HandlerExecutionViewResolverTest {
    private static final HandlerExecutionViewResolver handlerExecutionViewResolver =
            new HandlerExecutionViewResolver();
    private ModelAndView modelAndView;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    private void init() {
        modelAndView = new ModelAndView();
        modelAndView.setViewName("test.jsp");

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("핸들러가 HandlerExecution 이 아닐 경우 null 을 리턴")
    void handle() throws Exception {
        Controller controller = new HomeController();

        assertThat(handlerExecutionViewResolver.handle(controller, request, response)).isNull();
    }

    @Test
    @DisplayName("뷰를 리턴하지 않았다면, 뷰를 생성하여 mav 에 추가한 후 리턴한다.")
    void addView() throws Exception {
        Class<?> clazz = TestController.class;

        Method method = clazz.getDeclaredMethod("test", HttpServletRequest.class, HttpServletResponse.class);
        Object instance = clazz.getDeclaredConstructor(ModelAndView.class).newInstance(modelAndView);

        HandlerExecution handlerExecution = new HandlerExecution(new HandlerKey("/test", RequestMethod.GET), method, instance);

        assertThat(modelAndView.getView()).isNull();
        handlerExecutionViewResolver.handle(handlerExecution, request, response);
        assertThat(modelAndView.getView()).isNotNull();
    }
}