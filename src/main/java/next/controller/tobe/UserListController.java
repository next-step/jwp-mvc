/*
package next.controller.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserListController {
    private static final Logger log = LoggerFactory.getLogger(UserListController.class);

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create_string(String userId, String password) {
        log.debug("userId: {}, password: {}", userId, password);
        ModelAndView mav = new ModelAndView();
        mav.addObject("userId", userId);
        mav.addObject("password", password);
        return mav;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create_int_long(long id, int age) {
        log.debug("id: {}, age: {}", id, age);
        ModelAndView mav = new ModelAndView();
        mav.addObject("id", id);
        mav.addObject("age", age);
        return mav;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView createUser(User user) {
        log.debug("user: {}", user);
        ModelAndView mav = new ModelAndView();
        mav.addObject("testUser", user);
        return mav;
    }


    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ModelAndView show_pathvariable(@PathVariable long id) {
        log.debug("userId: {}", id);
        ModelAndView mav = new ModelAndView();
        mav.addObject("id", id);
        return mav;
    }
}
*/
