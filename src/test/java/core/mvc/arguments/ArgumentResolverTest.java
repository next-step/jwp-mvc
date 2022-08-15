package core.mvc.arguments;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import core.db.DataBase;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.ControllerExecutor;
import next.model.User;

public class ArgumentResolverTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.arguments");
        handlerMapping.initialize();
    }

    @Test
    void create_find() throws Exception {
        User user = new User("pobi", "123", "포비", "pobi@nextstep.camp");
        createUser(user);
        assertThat(DataBase.findUserById(user.getUserId())).isEqualTo(user);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        request.setParameter("userId", user.getUserId());
        MockHttpServletResponse response = new MockHttpServletResponse();
        ControllerExecutor execution = handlerMapping.getHandler(request);
        execution.execute(request, response);

        assertThat(response.getStatus()).isEqualTo(200);
    }

    private void createUser(User user) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());
        MockHttpServletResponse response = new MockHttpServletResponse();
        ControllerExecutor execution = handlerMapping.getHandler(request);
        execution.execute(request, response);
    }
}
