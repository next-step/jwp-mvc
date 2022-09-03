package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;

@Controller
public class JoinUserFormController {
    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView form() {
        return ModelAndView.fromJspView("/user/form.jsp");
    }
}
