package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static core.annotation.web.RequestMethod.GET;

@Controller
public class SignUpController {

    @RequestMapping(value = "/users/form", method = GET)
    public ModelAndView getSignUpForm(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("/user/form.jsp");
    }
}
