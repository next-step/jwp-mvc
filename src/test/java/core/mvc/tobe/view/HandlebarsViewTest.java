package core.mvc.tobe.view;

import next.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HandlebarsViewTest {

    @Test
    void handlebars() throws Exception {
        User user = new User("jun", "password", "hyunjun", "jun@test.com");
        List<User> users = new ArrayList<>();
        users.add(user);
        Map<String, List<User>> model = new HashMap<>();
        model.put("users", users);
        HandlebarsView view = new HandlebarsView("/user/handlebars-list");
        MockHttpServletResponse response = new MockHttpServletResponse();
        view.render(model, new MockHttpServletRequest(new MockServletContext()), response);
        assertThat(response.getContentAsString()).contains("jun", "hyunjun", "jun@test.com");

    }

}
