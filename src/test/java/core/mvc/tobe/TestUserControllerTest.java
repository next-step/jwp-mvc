package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import core.mvc.DispatcherServlet;
import core.web.context.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class TestUserControllerTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet(new ApplicationContext("core.mvc.tobe.TestUserController"));
        dispatcherServlet.init();
    }

    @DisplayName("/users String parameters")
    @Test
    void usersWithStringParameters() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest(RequestMethod.POST.name(), "/users/string");
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setParameter("userId", "userId");
        request.setParameter("password", "password");

        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @DisplayName("/users primitive type parameters")
    @Test
    void usersWithPrimitiveParameters() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest(RequestMethod.POST.name(), "/users/primitive");
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setParameter("id", "1");
        request.setParameter("age", "100");

        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @DisplayName("/users object type parameters")
    @Test
    void usersWithObject() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest(RequestMethod.POST.name(), "/users/object");
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setParameter("userId", "userId");
        request.setParameter("password", "password");
        request.setParameter("age", "100");

        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void pathVariableTest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest(RequestMethod.GET.name(), "/users/1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void requestParamTest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest(RequestMethod.GET.name(), "/users");
        request.setParameter("id", "1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
    }
}