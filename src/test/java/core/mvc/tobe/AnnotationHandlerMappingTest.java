package core.mvc.tobe;

import core.db.DataBase;
import core.mvc.scanner.WebApplicationScanner;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        WebApplicationScanner webApplicationScanner = new WebApplicationScanner("core.mvc.tobe");

        handlerMapping = new AnnotationHandlerMapping(webApplicationScanner);
        handlerMapping.initialize();
    }

    @Test
    public void create_find() throws Exception {
        /* given */
        User user = User.builder()
                .userId("pobi")
                .password("password")
                .name("포비")
                .email("pobi@nextstep.camp")
                .build();
        createUser(user);
        assertThat(DataBase.findUserById(user.getUserId())).isEqualTo(user);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        request.setParameter("userId", user.getUserId());

        MockHttpServletResponse response = new MockHttpServletResponse();

        /* when */
        HandlerMethod handlerMethod = handlerMapping.getHandlerMethod(request);
        handlerMethod.handle(request, response);

        /* then */
        assertThat(request.getAttribute("user")).isEqualTo(user);
    }

    private void createUser(User user) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerMethod handlerMethod = handlerMapping.getHandlerMethod(request);
        handlerMethod.handle(request, response);
    }
}
