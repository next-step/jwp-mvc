package next.controller;

import core.mvc.asis.DispatcherServlet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateUserControllerTest {

    @DisplayName("legacy -> annotation으로 변경 후 dispatcher servlet을 이용하여 CreateUserController 테스트")
    @Test
    public void request_method_test() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/create");
        MockHttpServletResponse response = new MockHttpServletResponse();
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        dispatcherServlet.service(request, response);

        assertEquals("/", response.getRedirectedUrl());
    }
}