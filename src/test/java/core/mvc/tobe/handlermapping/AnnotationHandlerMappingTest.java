package core.mvc.tobe.handlermapping;

import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.tobe.controller.MyController;
import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handler.HandlerExecutions;
import core.mvc.tobe.handlermapping.custom.AnnotationHandlerMapping;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.init();
    }

    @DisplayName("init() 메소드 호출 - AnnotationMappingHandler (handler 존재)")
    @ParameterizedTest
    @CsvSource(value = {"GET:/users/show", "POST:/users"}, delimiter = ':')
    void initForAnnotationHandlerMapping(String method, String url) throws ServletException, IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest(method, url);
        String basePackage = "core.mvc";

        //when
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        HandlerExecutions handlerExecutions = annotationHandlerMapping.init();

        //then
        HandlerExecution handler = handlerExecutions.findHandlerByUrlAndMethod(url, method);
        assertThat(handler).isNotNull();
    }

    @DisplayName("init() 메소드 호출 - AnnotationMappingHandler (handler 존재 X)")
    @ParameterizedTest
    @CsvSource(value = {"GET:/users/search", "POST:/members"}, delimiter = ':')
    void initForAnnotationHandlerMappingWhenNotMatched(String method, String url) throws ServletException, IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest(method, url);
        String basePackage = "core.mvc";

        //when
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        HandlerExecutions handlerExecutions = annotationHandlerMapping.init();

        //then
        HandlerExecution handler = handlerExecutions.findHandlerByUrlAndMethod(url, method);
        assertThat(handler).isNull();
    }

    @DisplayName("Request 객체 를 입력하면, mapping 된 컨트롤러를 가진 HandlerExecution 반환")
    @ParameterizedTest
    @CsvSource(value = {"POST:/users", "GET:/users", "GET:/users/show"}, delimiter = ':')
    void getHandler(String method, String uri) {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);

        //when
        HandlerExecution execution = handlerMapping.findHandler(request);

        //then
        assertThat(execution.getController()).isInstanceOf(MyController.class);
    }

    @DisplayName("요구사항 1 - 애노테이션 기반 프레임워크")
    @Test
    public void create_find() throws Exception {
        //given
        User user = new User("pobi", "password", "포비", "pobi@nextstep.camp");
        createUser(user);
        assertThat(DataBase.findUserById(user.getUserId())).isEqualTo(user);

        //when
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/show");
        request.setParameter("userId", user.getUserId());
        HandlerExecution execution = handlerMapping.findHandler(request);
        ModelAndView modelAndView = execution.handle(request, new MockHttpServletResponse());

        //then
        assertThat((User) modelAndView.getObject("user")).isEqualTo(user);
    }

    private void createUser(User user) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());

        HandlerExecution execution = handlerMapping.findHandler(request);
        execution.handle(request, new MockHttpServletResponse());
    }
}
