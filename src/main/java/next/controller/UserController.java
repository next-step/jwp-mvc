package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import core.mvc.ModelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created By kjs4395 on 2020-06-17
 */
@Controller
public class UserController {

    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView createUser(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView("/user/form.jsp");
    }
}
