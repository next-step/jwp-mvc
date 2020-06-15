package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
        User user = new User(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email"));
        logger.debug("User : {}", user);
        DataBase.addUser(user);

        ModelAndView modelAndView = new ModelAndView(new JspView("redirect:/users"));
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping("/users")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(new JspView("/users/list.jsp"));
        modelAndView.addObject("users", DataBase.findAll());

        return modelAndView;
    }

    @RequestMapping(value = "/users/show", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        logger.debug("UserId : {}", userId);
        User user = DataBase.findUserById(userId);

        ModelAndView modelAndView = new ModelAndView(new JspView("/users/show.jsp"));
        modelAndView.addObject("user", user);

        return modelAndView;
    }
}
