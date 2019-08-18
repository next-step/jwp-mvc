package core.mvc;

import core.mvc.view.JspView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.RequestDispatcher;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JspViewTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void render() throws Exception {
        String viewName = "/view/list.jsp";
        JspView view = new JspView(viewName);

        view.render(new HashMap<>(), request, response);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        assertThat(requestDispatcher).isNotNull();
        assertThat(response.getForwardedUrl()).isEqualTo(viewName);
    }


    @Test
    void render_model에_설정한_걊이_response에_전달된다() throws Exception {
        JspView view = new JspView("");

        Map<String, String> model = new HashMap<>();
        model.put("key1", "value1!!");
        model.put("key2", "value2!!");
        model.put("key3", "value3!!");

        view.render(model, request, response);

        model.forEach((key, value) -> {
            assertThat(request.getAttribute(key)).isEqualTo(value);
        });
    }
}