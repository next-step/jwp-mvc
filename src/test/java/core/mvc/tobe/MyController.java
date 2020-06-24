package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.model.User;
import next.view.UserCreateView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView findUserId(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        logger.debug("Find UserId : {}", userId);
        User user = DataBase.findUserById(userId);
        request.setAttribute("user", user);
        return null;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView save(UserCreateView userCreateView) {
        User user = userCreateView.toUser();
        logger.debug("User : {}", user);
        DataBase.addUser(user);
        return null;
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ModelAndView findUser(@PathVariable String userId) throws Exception {
        User user = DataBase.findUserById(userId);
        ModelAndView modelAndView = new ModelAndView(new JspView("/user/profile.jsp"));
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
