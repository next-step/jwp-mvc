package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class JoinUserFormController {
    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView form(HttpServletRequest request, HttpServletResponse response) {
        return ModelAndView.fromJspView("/user/form.jsp");
    }
}
