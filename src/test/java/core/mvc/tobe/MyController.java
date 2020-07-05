package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView findUserId(String userId) {
        logger.debug("Find UserId : {}", userId);
        User user = DataBase.findUserById(userId);
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView save(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        logger.debug("User : {}", user);
        DataBase.addUser(user);
        return null;
    }
}
