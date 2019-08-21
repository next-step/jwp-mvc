package next.controller;

import core.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HomeControllerTest {

    @DisplayName("home controller view model 검증")
    @Test
    void homeControllerTest() {
        HomeController homeController = new HomeController();
        ModelAndView mav = homeController.home();

        assertThat(mav.getView().getViewName()).isEqualTo("home");
        assertThat(mav.getModel()).containsKeys("users");
    }
}