package core.mvc.tobe;

import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
    }

    @AfterEach
    void tearDown() {
        DataBase.clear();
    }

    @Test
    public void create_find() throws Exception {
        User user = new User("pobi", "password", "포비", "pobi@nextstep.camp");
        createUser(user);
        assertThat(DataBase.findUserById(user.getUserId())).isEqualTo(user);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users-list");
        request.setParameter("userId", user.getUserId());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        assertThat(request.getAttribute("user")).isEqualTo(user);
    }

    @DisplayName("RequestMethod를 설정하지 않은 handler는 다른 RequestMethod 요청이 들어와도 수행한다")
    @Test
    void anyRequestMethod() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/request-method-test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        List<User> users = new ArrayList<>(DataBase.findAll());
        assertThat(users.get(0).getUserId()).isEqualTo("TEST");
    }

    @DisplayName("복수개의 메소드를 선언한 메소드에 POST 로 요청한다")
    @Test
    void postRequestMethod() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/post-and-get-test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        List<User> users = new ArrayList<>(DataBase.findAll());
        assertThat(users.get(0).getUserId()).isEqualTo("POST_AND_GET");
    }

    @DisplayName("복수개의 메소드를 선언한 메소드에 GET 으로 요청한다")
    @Test
    void getRequestMethod() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/post-and-get-test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        List<User> users = new ArrayList<>(DataBase.findAll());
        assertThat(users.get(0).getUserId()).isEqualTo("POST_AND_GET");
    }

    @DisplayName("argument가 String인 메소드 요청한다.")
    @Test
    void usersStringMethod() throws Exception {
        User user = new User("pobi", "password", "포비", "pobi@nextstep.camp");
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users-string");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);

        assertThat(modelAndView.getObject("userId")).isEqualTo(user.getUserId());
        assertThat(modelAndView.getObject("password")).isEqualTo(user.getPassword());
    }

    @DisplayName("argument가 int인 메소드를 요청한다.")
    @Test
    void usersIntMethod() throws Exception {
        long id = 1;
        int age = 10;
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users-int");
        request.setParameter("id", String.valueOf(id));
        request.setParameter("age", String.valueOf(age));
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo(id);
        assertThat(modelAndView.getObject("age")).isEqualTo(age);
    }

    @DisplayName("argument가 java bean인 메소드를 요청한다.")
    @Test
    void usersJavaBean() throws Exception {
        TestUser testUser = new TestUser("pobi", "password", 10);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.setParameter("userId", testUser.getUserId());
        request.setParameter("password", testUser.getPassword());
        request.setParameter("age", String.valueOf(testUser.getAge()));
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);

        assertThat(modelAndView.getObject("testUser")).isEqualTo(testUser);
    }

    @DisplayName("pathVariable 메소드를 요청한다.")
    @Test
    void usersPathVariable() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo(1L);
    }

    private void createUser(User user) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users-create");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }
}
