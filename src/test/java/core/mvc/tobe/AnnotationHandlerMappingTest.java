package core.mvc.tobe;

import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @DisplayName("Request 객체 를 입력하면, mapping 된 컨트롤러를 가진 HandlerExecution 반환")
    @ParameterizedTest
    @CsvSource(value = {"POST:/users", "GET:/users", "GET:/users/show"}, delimiter = ':')
    void getHandler(String method, String uri) {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);

        //when
        HandlerExecution execution = handlerMapping.getHandler(request);

        //then
        assertThat(execution.getController()).isInstanceOf(MyController.class);
    }


    @DisplayName("요구사항 1 - 애노테이션 기반 프레임워크")
    @Test
    public void create_find() throws Exception {
        User user = new User("pobi", "password", "포비", "pobi@nextstep.camp");
        createUser(user);
        assertThat(DataBase.findUserById(user.getUserId())).isEqualTo(user);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/show");
        request.setParameter("userId", user.getUserId());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);

        assertThat((User)modelAndView.getObject("user")).isEqualTo(user);
    }

    private void createUser(User user) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }
}
