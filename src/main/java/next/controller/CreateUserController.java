package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.view.ModelAndView;
import core.mvc.view.RedirectView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CreateUserController {
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @RequestMapping("/users/create")
    public ModelAndView execute(@RequestParam String userId, @RequestParam String password, @RequestParam String name, @RequestParam String email) throws Exception {
        User user = new User(userId, password, name, email);
        log.debug("User : {}", user);

        DataBase.addUser(user);
        return new ModelAndView(new RedirectView("/"));
    }
}
