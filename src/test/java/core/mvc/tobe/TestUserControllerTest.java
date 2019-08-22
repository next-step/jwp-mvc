package core.mvc.tobe;

import core.mvc.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUserControllerTest {
    private static final String BASE_PACKAGE = "core.mvc.tobe";
    private static final String REQUEST_URI_STRINGS = "/users/strings";
    private static final String REQUEST_URI_PRIMITIVES = "/users/primitives";
    private static final String REQUEST_URI_TESTUSER = "/users/testUser";
    private static final String REQUEST_URI_PATH_VAR = "/users/";
    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private static final String ID = "id";
    private static final String AGE = "age";

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        HandlerMethodArgumentResolvers.getInstance()
                .add(new PathVariableArgumentResolver())
                .add(new ParamNameArgumentResolver())
                .add(new HttpRequestArgumentResolver())
                .add(new HttpResponseArgumentResolver())
                .add(new TestUserArgumentResolver());

        handlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        handlerMapping.initialize();
    }

    @DisplayName("RequestMapping argument resolve : String 테스트")
    @Test
    public void stringsMethodTest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", REQUEST_URI_STRINGS);
        MockHttpServletResponse response = new MockHttpServletResponse();

        String userId = "circlee";
        String password = "alskmdlkd";

        request.setParameter(USER_ID, userId);
        request.setParameter(PASSWORD, password);

        HandlerExecution execution = handlerMapping.getHandler(request);


        ModelAndView modelAndView = execution.handle(request, response);

        Map<String, Object> model = modelAndView.getModel();
        assertThat(model).isNotNull();
        assertThat(model.get(USER_ID)).isEqualTo(userId);
        assertThat(model.get(PASSWORD)).isEqualTo(password);
    }

    @DisplayName("RequestMapping argument resolve : primitive type 테스트")
    @Test
    public void primitiveMethodTest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", REQUEST_URI_PRIMITIVES);
        MockHttpServletResponse response = new MockHttpServletResponse();

        long id = 3;
        int age = 19;

        request.setParameter(ID, String.valueOf(id));
        request.setParameter(AGE, String.valueOf(age));

        HandlerExecution execution = handlerMapping.getHandler(request);


        ModelAndView modelAndView = execution.handle(request, response);

        Map<String, Object> model = modelAndView.getModel();
        assertThat(model).isNotNull();
        assertThat(model.get(ID)).isEqualTo(id);
        assertThat(model.get(AGE)).isEqualTo(age);
    }

    @DisplayName("RequestMapping argument resolve : TestUser Class 테스트")
    @Test
    public void testUserMethodTest() throws Exception {
        String userId = "circlee";
        String password = "alskmdlkd";
        int age = 19;

        MockHttpServletRequest request = new MockHttpServletRequest("POST", REQUEST_URI_TESTUSER);
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter(USER_ID, userId);
        request.setParameter(PASSWORD, password);
        request.setParameter(AGE, String.valueOf(age));
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);

        Map<String, Object> model = modelAndView.getModel();
        assertThat(model).isNotNull();
        TestUser testUser = (TestUser)model.get("testUser");
        assertThat(testUser).isNotNull();
        assertThat(testUser.getUserId()).isEqualTo(userId);
        assertThat(testUser.getPassword()).isEqualTo(password);
        assertThat(testUser.getAge()).isEqualTo(age);
    }

    @DisplayName("RequestMapping argument resolve : path variable 테스트")
    @Test
    public void pathVariableMethodTest() throws Exception {
        int id = 19;
        MockHttpServletRequest request = new MockHttpServletRequest("GET", REQUEST_URI_PATH_VAR + id + "/id");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);

        Map<String, Object> model = modelAndView.getModel();
        assertThat(model).isNotNull();
        assertThat(Long.parseLong(model.get(ID).toString())).isEqualTo(id);
    }

}
