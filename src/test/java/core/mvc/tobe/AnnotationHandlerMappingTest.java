package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import core.configuration.ApplicationContext;
import core.db.DataBase;
import core.web.view.ModelAndView;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping annotationHandlerMapping;

    @BeforeEach
    public void setup() {
        ApplicationContext.getInstance().init();

        annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.init();
    }

    @Test
    public void create_find() throws Exception {
        User user = new User("pobi", "password", "포비", "pobi@nextstep.camp");
        createUser(user);
        assertThat(DataBase.findUserById(user.getUserId())).isEqualTo(user);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        HandlerKey handlerKey = new HandlerKey("/users", RequestMethod.GET);
        request.setParameter("userId", user.getUserId());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = (HandlerExecution) annotationHandlerMapping.getHandler(handlerKey);
        ModelAndView modelAndView = execution.handle(request, response);

        assertThat(modelAndView.getModel().get("user")).isEqualTo(user);
    }

    private void createUser(User user) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/create");
        HandlerKey handlerKey = new HandlerKey("/users/create", RequestMethod.POST);
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = (HandlerExecution) annotationHandlerMapping.getHandler(handlerKey);
        execution.handle(request, response);
    }
}
