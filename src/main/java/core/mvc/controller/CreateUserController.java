package core.mvc.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.model.User;

@Controller
public class CreateUserController {

    @RequestMapping(value = "/users/form")
    public ModelAndView form() throws Exception {
        return new ModelAndView(new JspView("/user/form.jsp"));
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(@RequestParam String userId, @RequestParam String password,
        @RequestParam String name, @RequestParam String email) throws Exception {

        User user = new User(userId, password, name, email);
        DataBase.addUser(user);

        return new ModelAndView(new JspView("redirect:/"));
    }
}
