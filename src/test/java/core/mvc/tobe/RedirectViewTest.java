package core.mvc.tobe;

import core.mvc.tobe.view.RedirectView;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class RedirectViewTest {
    private static final Logger logger = LoggerFactory.getLogger(RedirectViewTest.class);

    @Test
    void create() throws Exception {
        String pathName = "redirect:/user";
        RedirectView redirectView = new RedirectView(pathName);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        redirectView.render(null, request, response);

        assertThat(response.getRedirectedUrl()).isEqualTo(pathName);
        assertThat(response.getForwardedUrl()).isEqualTo(null);
    }
}
