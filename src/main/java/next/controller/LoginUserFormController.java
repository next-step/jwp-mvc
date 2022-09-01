package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;

@Controller
public class LoginUserFormController {
    @RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
    public ModelAndView form() {
        return ModelAndView.fromJspView("/user/login.jsp");
    }
}
