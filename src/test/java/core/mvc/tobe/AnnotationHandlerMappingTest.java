package core.mvc.tobe;

import core.db.DataBase;
import core.mvc.DispatcherServlet;
import core.mvc.handlerMapping.AnnotationHandlerMapping;
import core.mvc.handler.HandlerExecution;
import core.mvc.support.HandlerMethodArgumentResolverComposite;
import core.mvc.support.ModelAttributeResolver;
import core.mvc.support.RequestParamResolver;
import core.mvc.support.ServletRequestResolver;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationHandlerMappingTest {
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    public void setup() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @Test
    public void create_find() throws Exception {
        // given
        final User user = new User("pobi", "password", "포비", "pobi@nextstep.camp");
        createUser(user);
        assertThat(DataBase.findUserById(user.getUserId())).isEqualTo(user);

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/profile");
        request.setParameter("userId", user.getUserId());

        dispatcherServlet.service(request, response);

        assertThat(request.getAttribute("user")).isEqualTo(user);
    }

    private void createUser(User user) throws Exception {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/create");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());

        dispatcherServlet.service(request, response);
    }
}
