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

    @DisplayName("asis 패키지의 기존 MVC 프레임워크와 tobe 패키지의 새로운 MVC 프레임워크의 동작으로 생성된 결과값이 같은지 확인한다.")
    @Test
    void checkSameResponse() throws ServletException, IOException {
        MockHttpServletResponse asisResponse = new MockHttpServletResponse();
        AbstractDispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        dispatcherServlet.service(request, asisResponse);

        MockHttpServletResponse tobeResponse = new MockHttpServletResponse();
        AbstractDispatcherServlet newDispatcherServlet = new NewDispatcherServlet();
        newDispatcherServlet.init();
        dispatcherServlet.service(request, tobeResponse);

        assertAll(
                () -> assertThat(asisResponse.getHeader("Location")).isEqualTo(tobeResponse.getHeader("Location")),
                () -> assertThat(asisResponse.getStatus()).isEqualTo(tobeResponse.getStatus()),
                () -> assertThat(asisResponse.getErrorMessage()).isEqualTo(tobeResponse.getErrorMessage()),
                () -> assertThat(asisResponse.getForwardedUrl()).isEqualTo(tobeResponse.getForwardedUrl()),
                () -> assertThat(asisResponse.getIncludedUrl()).isEqualTo(tobeResponse.getIncludedUrl())
        );
    }




}