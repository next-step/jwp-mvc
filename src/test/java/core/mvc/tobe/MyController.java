package core.mvc.tobe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;

@Controller
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView findUserId(String userId) {
        logger.debug("Find UserId : {}", userId);
        User user = DataBase.findUserById(userId);
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView.addObject("user", user);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView save(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        logger.debug("User : {}", user);
        DataBase.addUser(user);
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView.addObject("user", user);
    }

    @RequestMapping(value = "/testAllMethods")
    public ModelAndView testAllMethod() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("status", "ok");
        return modelAndView;
    }
}
