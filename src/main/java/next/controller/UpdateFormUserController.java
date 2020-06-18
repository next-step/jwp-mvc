package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.view.ModelAndView;
import next.model.User;

import javax.servlet.http.HttpSession;

@Controller
public class UpdateFormUserController {

    @RequestMapping(value = "/users/updateForm", method = RequestMethod.GET)
    public ModelAndView updateForm(String userId, HttpSession userSession) throws Exception {
        User user = DataBase.findUserById(userId);

        if (!UserSessionUtils.isSameUser(userSession, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("/user/updateForm.jsp");

        return modelAndView;
    }
}
