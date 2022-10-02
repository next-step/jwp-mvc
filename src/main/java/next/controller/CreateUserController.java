package next.controller;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.asis.Controller;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@core.annotation.web.Controller
public class CreateUserController {
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @RequestMapping(value = "/users/create")
    public String execute(@RequestParam String userId, String password, String name, String email) throws Exception {
        User user = new User(
                userId, password, name, email);
        log.debug("User : {}", user);

        DataBase.addUser(user);
        return "redirect:/";
    }
}