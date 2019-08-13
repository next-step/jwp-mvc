package core.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class RedirectViewTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void render() throws Exception {
        String viewName = "/view/list";
        RedirectView view = new RedirectView(viewName);

        view.render(null, request, response);

        assertThat(response.getHeader(HttpHeaders.LOCATION)).isEqualTo(viewName);
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_MOVED_TEMPORARILY);
    }

    @DisplayName("viewName 이 redirect: 로 시작하면 redirect: 를 제거한다.")
    @Test
    void render_startsWith_viewName() throws Exception {
        String viewName = "redirect:/view/list";
        String expectedViewName = "/view/list";

        RedirectView view = new RedirectView(viewName);

        view.render(null, request, response);
        assertThat(response.getHeader(HttpHeaders.LOCATION)).isEqualTo(expectedViewName);
    }
}