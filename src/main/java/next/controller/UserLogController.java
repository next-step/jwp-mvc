package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserLogController {

    private static final Logger logger = LoggerFactory.getLogger(UserLogController.class);

    @RequestMapping("/user/log")
    public ModelAndView userLog(User user) {
        logger.info("user: {}", user.toString());
        return new ModelAndView("forward:home.jsp");
    }

    @RequestMapping("/user/info/{userId}")
    public ModelAndView userInfo(@PathVariable(name = "userId") String id) {
        logger.info("userId: {}", id);
        return new ModelAndView("forward:home.jsp");
    }

}
