package core.mvc.asis;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DispatcherServletTest {
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest("POST", "/users/create");
        User user = new User("pobi", "password", "포비", "pobi@nextstep.camp");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());
    }

    @DisplayName("tobe 패키지의 새로운 MVC 프레임워크가 정상적으로 동작하는지 확인한다.")
    @Test
    void integrationMVCFramework() throws ServletException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        dispatcherServlet.service(request, response);

        assertAll(
                () -> assertThat(response.getHeader("Location")).isEqualTo("/"),
                () -> assertThat(response.getStatus()).isEqualTo(302),
                () -> assertThat(response.getErrorMessage()).isNull(),
                () -> assertThat(response.getForwardedUrl()).isNull(),
                () -> assertThat(response.getIncludedUrl()).isNull()
        );
    }

    @DisplayName("tobe 패키지의 새로운 MVC 프레임워크가 정상적으로 동작하는지 확인한다.")
    @Test
    void integrationMVCFramework_useAsis() throws ServletException {
        HttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        dispatcherServlet.service(request, response);

        assertAll(
                () -> assertThat(response.getHeader("Location")).isEqualTo("/users/loginForm"),
                () -> assertThat(response.getStatus()).isEqualTo(302),
                () -> assertThat(response.getErrorMessage()).isNull(),
                () -> assertThat(response.getForwardedUrl()).isNull(),
                () -> assertThat(response.getIncludedUrl()).isNull()
        );
    }
}