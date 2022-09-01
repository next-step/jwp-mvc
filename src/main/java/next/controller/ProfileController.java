package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public ModelAndView profille(String userId, HttpServletRequest request) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        request.setAttribute("user", user);
        return ModelAndView.fromJspView("/user/profile.jsp");
    }
}
