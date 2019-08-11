package next.controller;

import core.mvc.ModelAndView;
import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class HomeControllerTest extends AbstractController{

    @DisplayName("어노테이션기반으로 homeController 실행")
    @Test
    void home() throws Exception {
        ModelAndView modelAndView = execute("GET", "/");

        Collection<User> users = (Collection<User>) modelAndView.getObject("users");

        assertThat(users).hasSize(1);
    }
}