package core.mvc.tobe;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelAndViewWrapperTest {
    @Test
    void wrapTest() throws ReflectiveOperationException {
        String viewName = "/user/list.jsp";
        ModelAndView modelAndView = ModelAndViewWrapper.wrap(viewName, JspView.class);

        assertThat(modelAndView.getViewName()).isEqualTo(viewName);
    }
}
