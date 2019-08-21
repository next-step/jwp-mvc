package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.model.User;

@Controller
public class ProfileController {
    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public ModelAndView execute(String userId) throws Exception {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        ModelAndView modelAndView = new ModelAndView(new JspView("/user/profile.jsp"));
        modelAndView.addObject("user", user);

        return modelAndView;
    }
}
