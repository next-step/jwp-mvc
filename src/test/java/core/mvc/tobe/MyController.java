package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.tobe.view.ModelAndView;
import core.mvc.tobe.view.SimpleNameView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import static core.annotation.web.RequestMethod.GET;

@Controller
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView findUserId(HttpServletRequest request, String userId) {
        logger.debug("Find UserId : {}", userId);
        User user = DataBase.findUserById(userId);
        request.setAttribute("user", user);
        return new ModelAndView(new SimpleNameView("/user/list.jsp"));
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView save(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        logger.debug("User : {}", user);
        DataBase.addUser(user);
        return new ModelAndView(new SimpleNameView("redirect:/"));
    }

    @RequestMapping(value = "/users/{id}", method = GET)
    public String pathPatternMethod(@PathVariable Long id) {
        return "redirect:/";
    }
}
