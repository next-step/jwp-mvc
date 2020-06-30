package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;

import static core.annotation.web.RequestMethod.GET;

@Controller
public class SignUpController {

    @RequestMapping(value = "/users/form", method = GET)
    public ModelAndView getSignUpForm() {
        return new ModelAndView("/user/form.jsp");
    }
}
