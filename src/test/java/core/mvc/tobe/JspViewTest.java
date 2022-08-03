package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JspViewTest {

    @DisplayName("model을 포함하여 페이지로 이동한다.")
    @Test
    void forward() throws Exception {
        final Map<String, Object> model = Map.of("hi", "안녕");

        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        final JspView jspView = new JspView("/home.jsp");
        jspView.render(model, request, response);

        assertAll(
            () -> assertThat(request.getAttribute("hi")).isEqualTo("안녕"),
            () -> assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK),
            () -> assertThat(response.getForwardedUrl()).isEqualTo("/home.jsp")
        );

    }

    @DisplayName("다른 위치로 이동시킨다")
    @Test
    void redirect() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        final JspView jspView = new JspView("redirect:/home.jsp");
        jspView.render(null, request, response);

        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FOUND),
            () -> assertThat(response.getHeader("Location")).isEqualTo("/home.jsp")
        );

    }
}
