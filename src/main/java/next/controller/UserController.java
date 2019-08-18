package next.controller;


import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.view.JspView;
import core.mvc.view.RedirectView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(User user) throws Exception {
        log.debug("User : {}", user);

        DataBase.addUser(user);
        return new ModelAndView(new RedirectView("redirect:/"));
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ModelAndView profile(@PathVariable String userId) throws Exception {
        log.debug("userId : {}", userId);
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        ModelAndView mav = new ModelAndView(new JspView("/user/profile.jsp"));
        mav.addObject("user", user);
        return mav;
    }
}
