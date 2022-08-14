package core.mvc.tobe.handler.mapping;

import core.db.DataBase;
import core.mvc.tobe.ControllerScanner;
import core.mvc.tobe.MyController;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping(
                new ControllerScanner("core.mvc.tobe")
        );
        handlerMapping.initialize();
    }

    @Test
    public void create_find() throws Exception {
        User user = new User("pobi", "password", "포비", "pobi@nextstep.camp");
        createUser(user);
        assertThat(DataBase.findUserById(user.getUserId())).isEqualTo(user);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        request.setParameter("userId", user.getUserId());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        assertThat(request.getAttribute("user")).isEqualTo(user);
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

    @DisplayName("RequestMapping에 지정된 url이 패턴형식인 경우, 패턴이 일치하고, method가 동일한 핸들러를 반환한다.")
    @Test
    void mapping_pattern_url() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/2");
        HandlerExecution execution = handlerMapping.getHandler(request);


        MyController myController = new MyController();
        Method realMethod = Arrays.stream(MyController.class.getDeclaredMethods())
                .filter(method -> "pathPatternMethod".equals(method.getName()))
                .findFirst()
                .get();

        assertThat(execution).usingRecursiveComparison()
                .isEqualTo(new HandlerExecution(myController, realMethod));

    }
}
