package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;

/**
 * @author KingCjy
 */

@Controller
@RequestMapping("/test")
public class TestAnnotationController {

    @RequestMapping("/users")
    public ModelAndView getUsers() {
        return new ModelAndView("users.jsp");
    }
}
