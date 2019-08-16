package next.controller;

import core.mvc.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class HomeControllerTest {

    @DisplayName("home controller view model 검증")
    @Test
    void homeControllerTest() {
        HomeController homeController = new HomeController();
        ModelAndView mav = homeController.home(new MockHttpServletRequest(), new MockHttpServletResponse());

        assertThat(mav.getView().getViewName()).isEqualTo("home");
        assertThat(mav.getModel()).containsKeys("users");
    }
}