package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CreateUserController {
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @RequestMapping("/users/form")
    public String getJoinForm(HttpServletRequest request, HttpServletResponse response) {
        return "/user/form.jsp";
    }

    @RequestMapping("/users/create")
    public String createUser(HttpServletRequest request, HttpServletResponse response) {
        User user = new User(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"),
                request.getParameter("email"));
        log.debug("User : {}", user);

        DataBase.addUser(user);
        return "redirect:/";
    }
}
