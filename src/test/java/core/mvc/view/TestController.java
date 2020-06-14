package core.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestController {
    private final ModelAndView modelAndView;

    public TestController(final ModelAndView modelAndView) {
        this.modelAndView = modelAndView;
    }

    public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
        return modelAndView;
    }
}
