package next.controller.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TestController {
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
