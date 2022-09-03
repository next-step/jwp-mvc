package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UpdateUserFormController {
    @RequestMapping(value = "/users/updateForm", method = RequestMethod.GET)
    public ModelAndView form(String userId, HttpServletRequest request) {
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        request.setAttribute("user", user);
        return ModelAndView.fromJspView("/user/updateForm.jsp");
    }
}
