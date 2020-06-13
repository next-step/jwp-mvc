package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UpdateFormUserController {

    @RequestMapping(value = "/users/updateForm", method = RequestMethod.GET)
    public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("user", user);
        mv.setViewName("/user/updateForm.jsp");

        return mv;
    }
}
