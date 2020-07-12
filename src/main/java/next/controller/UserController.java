package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.dto.UserDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(UserDto userDto) {
        log.debug("User : {}", userDto);
        User user = userDto.toEntity();
        DataBase.addUser(user);
        return new ModelAndView("redirect:/");
    }
}
