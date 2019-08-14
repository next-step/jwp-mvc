package core.mvc;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelAndViewTest {

    @Test
    void mavGetAndSet() {
        String viewName = "jun.jsp";
        ModelAndView mav = new ModelAndView(viewName)
                .addObject("name", "jun")
                .addObject("addr", "seoul");

        assertThat(mav.getObject("name")).isEqualTo("jun");
        assertThat(mav.getObject("addr")).isEqualTo("seoul");
        assertThat(mav.getViewName()).isEqualTo("jun.jsp");
    }

}
