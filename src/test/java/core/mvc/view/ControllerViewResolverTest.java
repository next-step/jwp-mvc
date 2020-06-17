package core.mvc.view;

import core.mvc.tobe.HandlerExecution;
import next.controller.HomeController;
import next.controller.ListUserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("컨트롤러 인터페이스를 구현한 객체를 실행하고 그 리턴값으로 뷰를 생성")
class ControllerViewResolverTest {
    private final ControllerViewResolver controllerViewResolver = new ControllerViewResolver();
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    private void init() {
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("핸들러가 컨트롤러가 아닌 경우 null 을 리턴")
    void handle() throws Exception {
        Class<?> clazz = HomeController.class;
        Method method = clazz.getDeclaredMethod("execute", HttpServletRequest.class, HttpServletResponse.class);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        HandlerExecution handlerExecution = new HandlerExecution(method, instance);

        ModelAndView handle = controllerViewResolver.handle(handlerExecution, request, response);

        assertThat(handle).isNull();
    }

    @Test
    @DisplayName("Jsp 를 서빙하는 경우 view 가 있는 ModelAndView 를 리턴한다")
    void handleWithController() throws Exception {
        HomeController homeController = new HomeController();

        ModelAndView modelAndView = controllerViewResolver.handle(homeController, request, response);

        assertThat(modelAndView.getView()).isNotNull();
    }

    @Test
    @DisplayName("리다이렉트를 하는 경우 DummyView 를 포함한 ModelAndView 를 리턴한다")
    void handleRedirectCase() throws Exception {
        ListUserController listUserController = new ListUserController();

        ModelAndView modelAndView = controllerViewResolver.handle(listUserController, request, response);

        assertThat(modelAndView.getView()).isEqualTo(DummyView.INSTANCE);
    }
}