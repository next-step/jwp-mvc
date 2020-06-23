package core.mvc.tobe;

import core.db.DataBase;
import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
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

    @Test
    public void createUserTestByParameter() throws Exception{
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("userId", "kjs4395");
        request.addParameter("password", "password");

        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView modelAndView = execution.handle(request, response);

        assertEquals(modelAndView.getObject("userId"),"kjs4395");
        assertEquals(modelAndView.getObject("password"), "password");
    }

    @Test
    public void noArgsTest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView modelAndView = execution.handle(request, response);
    }

    @Test
    public void primitiveTypeTest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("id", "333");
        request.addParameter("age", "29");

        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView modelAndView = execution.handle(request, response);

        assertEquals(modelAndView.getObject("id"), (long) 333);
        assertEquals(modelAndView.getObject("age"), 29);
    }

    @Test
    public void objectParameterInvokeTest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
/*
        TestUser testUser = new TestUser("kjs4395", "1234", 29);

        request.setAttribute("testUser", testUser);
*/
        //request.addParameter("testUser", testUser);
        request.addParameter("userId", "kjs4395");
        request.addParameter("password","1234");
        request.addParameter("age","29");
        HandlerExecution execution = handlerMapping.getHandler(request);

        ModelAndView modelAndView = execution.handle(request, response);
        TestUser testUser = (TestUser) modelAndView.getObject("testUser");

        assertEquals(testUser.getPassword(),"1234");
    }
}
