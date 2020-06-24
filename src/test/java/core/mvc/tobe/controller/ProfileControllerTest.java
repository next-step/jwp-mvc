package core.mvc.tobe.controller;

import core.db.DataBase;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileControllerTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @Test
    void logout() throws Exception {
        User user = new User("pobi", "password", "포비", "pobi@nextstep.camp");
        createUser(user);
        assertThat(DataBase.findUserById(user.getUserId())).isEqualTo(user);

        MockHttpServletRequest requestGet = new MockHttpServletRequest("GET", "/users/profile");
        requestGet.setParameter("userId", "pobi");

        MockHttpServletRequest requestPost = new MockHttpServletRequest("POST", "/users/profile");
        requestPost.setParameter("userId", "pobi");

        MockHttpServletRequest requestPut = new MockHttpServletRequest("PUT", "/users/profile");
        requestPut.setParameter("userId", "pobi");

        MockHttpServletRequest requestPatch = new MockHttpServletRequest("PATCH", "/users/profile");
        requestPatch.setParameter("userId", "pobi");

        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution executionGet = handlerMapping.getHandler(requestGet);
        HandlerExecution executionPost = handlerMapping.getHandler(requestPost);
        HandlerExecution executionPut = handlerMapping.getHandler(requestPut);
        HandlerExecution executionPatch = handlerMapping.getHandler(requestPatch);

        executionGet.handle(requestGet, response);
        executionPost.handle(requestPost, response);
        executionPut.handle(requestPut, response);
        executionPatch.handle(requestPatch, response);
    }

    private void createUser(User user) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/create");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }
}
