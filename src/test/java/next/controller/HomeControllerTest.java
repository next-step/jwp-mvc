package next.controller;

import core.mvc.ModelAndView;
import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static next.controller.AnnotationController.execute;
import static org.assertj.core.api.Assertions.assertThat;

class HomeControllerTest {

    @DisplayName("메인페이지 접속 시 유저 리스트를 가져오는데 성공한다")
    @Test
    void home() {
        ModelAndView modelAndView = execute("GET", "/");

        Collection<User> users = (Collection<User>) modelAndView.getObject("users");

        assertThat(users).hasSize(1);
    }
}