package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.view.JspView;
import core.mvc.view.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class NewCreateUserController {

    private static final Logger log = LoggerFactory.getLogger(NewCreateUserController.class);

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView createUser(User user) {
        log.debug("new user: {}", user);
        DataBase.addUser(user);
        return new ModelAndView(new JspView("/home.jsp"));
    }
}
