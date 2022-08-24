package next.controller;

import core.db.DataBase;
import core.mvc.asis.Controller;
import core.mvc.view.ModelAndView;
import core.mvc.view.ResourceView;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LegacyProfileController implements Controller {
    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        req.setAttribute("user", user);
        return new ModelAndView(new ResourceView("/user/profile.jsp"));
    }
}
