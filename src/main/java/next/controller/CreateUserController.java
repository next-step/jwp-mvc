package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CreateUserController {
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @RequestMapping(value = "/users/create")
    public ModelAndView execute(User user) throws Exception {
        log.debug("User : {}", user);

        DataBase.addUser(user);
        
        ModelAndView modelAndView = new ModelAndView(new JspView("redirect:/"));
        return modelAndView;
    }
}
