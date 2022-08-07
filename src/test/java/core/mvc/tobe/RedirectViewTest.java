package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import core.mvc.View;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class RedirectViewTest {

    @DisplayName("다른 위치로 이동시킨다")
    @Test
    void redirect() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        final View redirectView = new RedirectView("redirect:/home.jsp");
        redirectView.render(null, request, response);

        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FOUND),
            () -> assertThat(response.getHeader("Location")).isEqualTo("/home.jsp")
        );

    }
}
