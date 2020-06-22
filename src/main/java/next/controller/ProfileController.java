package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ProfileController {

    @RequestMapping("/users/profile")
    public String getUser(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        request.setAttribute("user", user);
        return "/user/profile.jsp";
    }
}
