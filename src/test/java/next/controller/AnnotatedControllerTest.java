package next.controller;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnnotatedControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(AnnotatedControllerTest.class);
    private AnnotationHandlerMapping handlerMapping;

    private static Stream requestProvider() {
        return Stream.of(
                Arguments.of("/users", "GET")
        );
    }

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("next.controller");
        handlerMapping.initialize();
    }

    @DisplayName("@Controller 와 @RequestMapping 으로 설정하여 요청에 대한 응답")
    @ParameterizedTest(name = "RequestURI: {0}, RequestMethod: {1}")
    @MethodSource("requestProvider")
    public void getHandler(String requestURI, String method) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, requestURI);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = execution.handle(request, response);
        assertThat(modelAndView.getClass()).isEqualTo(ModelAndView.class);
    }

    @DisplayName("요청에 대한 핸들러를 찾을 수 없을 때 Null 리턴")
    @Test
    public void shouldNull_When_NotFound() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        assertThat(execution).isNull();
    }

    @DisplayName("유저 생성")
    @Test
    public void createUser() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        User createdUser = new User("myId", "myPass", "myName", "myEmail");
        request.setParameter("userId", createdUser.getUserId());
        request.setParameter("password", createdUser.getPassword());
        request.setParameter("name", createdUser.getName());
        request.setParameter("email", createdUser.getEmail());
        HandlerExecution handler = handlerMapping.getHandler(request);
        handler.handle(request, response);

        request = new MockHttpServletRequest("GET", "/users/profile");
        response = new MockHttpServletResponse();
        request.setParameter("userId", createdUser.getUserId());
        handler = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handler.handle(request, response);
        final Map<String, Object> model = modelAndView.getModel();
        final User user = (User) model.get("user");

        assertAll("조회한 유저는 생성한 유저와 같음",
                () -> assertThat(user.getName()).isEqualTo(createdUser.getName()),
                () -> assertThat(user.getEmail()).isEqualTo(createdUser.getEmail())
        );
    }

    @DisplayName("로그인한 사용자는 유저 목록을 조회")
    @Test
    public void should_getUsers_before_login() throws Exception {
        final HttpSession loginSession = getLoginSession("admin", "password");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        request.setSession(loginSession);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution handler = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handler.handle(request, response);
        final Map<String, Object> model = modelAndView.getModel();
        final Collection users = (Collection) model.get("users");

        assertThat(users.size()).isNotZero();
    }

    @DisplayName("로그인하지 않은 사용자는 유저 목록을 가져오지 않음")
    @Test
    public void should_fail_getUsers_not_login() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution handler = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handler.handle(request, response);
        final Map<String, Object> model = modelAndView.getModel();
        final Object users = model.get("users");

        assertThat(users).isNull();
    }

    private HttpSession getLoginSession(String userId, String password) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("userId", userId);
        request.setParameter("password", password);
        HandlerExecution handler = handlerMapping.getHandler(request);
        handler.handle(request, response);
        return request.getSession();
    }
}
