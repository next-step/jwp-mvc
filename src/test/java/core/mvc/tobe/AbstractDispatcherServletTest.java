package core.mvc.tobe;

import core.mvc.asis.DispatcherServlet;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AbstractDispatcherServletTest {

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
    void tobeMVCFramework() throws ServletException, IOException {
        MockHttpServletResponse tobeResponse = new MockHttpServletResponse();
        AbstractDispatcherServlet newDispatcherServlet = new NewDispatcherServlet();
        newDispatcherServlet.init();
        newDispatcherServlet.service(request, tobeResponse);

        assertAll(
                () -> assertThat(tobeResponse.getHeader("Location")).isEqualTo("/"),
                () -> assertThat(tobeResponse.getStatus()).isEqualTo(302),
                () -> assertThat(tobeResponse.getErrorMessage()).isNull(),
                () -> assertThat(tobeResponse.getForwardedUrl()).isNull(),
                () -> assertThat(tobeResponse.getIncludedUrl()).isNull()
        );
    }
}