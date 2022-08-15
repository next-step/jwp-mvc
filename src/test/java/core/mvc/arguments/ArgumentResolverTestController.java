package core.mvc.arguments;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;

@Controller
public class ArgumentResolverTestController {
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView findUserId(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        User user = DataBase.findUserById(userId);
        request.setAttribute("user", user);
        return null;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView findByUserId(String userId) {
        User user = DataBase.findUserById(userId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) {
        User user = new User(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email"));
        DataBase.addUser(user);
        return null;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView createUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
        return null;
    }
}
