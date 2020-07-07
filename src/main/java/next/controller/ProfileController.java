package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;

@Controller
public class ProfileController {

    @RequestMapping(value = "/users/profile/{userId}", method = RequestMethod.GET)
    public ModelAndView execute(@PathVariable String userId) throws Exception {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        mav.setView("/user/profile.jsp");
        return mav;
    }
}
