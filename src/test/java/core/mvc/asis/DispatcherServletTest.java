package core.mvc.asis;

import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() throws ServletException {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @Test
    void 정상_동작_확인() throws ServletException, IOException {
        HttpServletRequest request = new MockHttpServletRequest(RequestMethod.GET.name(), "/");
        HttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);
    }
}