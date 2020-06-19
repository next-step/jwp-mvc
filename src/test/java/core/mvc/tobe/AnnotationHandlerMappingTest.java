package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.HandlerMapping;
import core.mvc.exceptions.HandlerNotFoundException;
import core.mvc.view.ModelAndView;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
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

    @DisplayName("핸들러가 존재하지 않으면 HandlerNotFoundException이 발생해요!")
    @Test
    public void throws_if_handler_not_exist() throws Exception {
        final MockHttpServletRequest request =
                new MockHttpServletRequest("GET", "/no_such_path_exist");
        assertThatThrownBy(() -> handlerMapping.getHandler(request))
                .isInstanceOf(HandlerNotFoundException.class);
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

    @DisplayName("파라미터가 다르더라도 같은 pattern이면 핸들러도 동일해야함")
    @Test
    public void test_path_variable_mapping() throws Exception {
        final HandlerMapping handlerMapping = new AnnotationHandlerMapping(PathVariableController.class);
        final MockHttpServletRequest request1 = new MockHttpServletRequest("GET", "/test/1");
        final MockHttpServletRequest request2 = new MockHttpServletRequest("GET", "/test/2");
        final Object handler1 = handlerMapping.getHandler(request1);
        final Object handler2 = handlerMapping.getHandler(request2);
        assertThat(handler1).isEqualTo(handler2);
    }

    @Controller
    static class PathVariableController {

        @RequestMapping(value = "/test/{id}", method = RequestMethod.GET)
        public ModelAndView test(@PathVariable long id) {
            return null;
        }
    }
}
