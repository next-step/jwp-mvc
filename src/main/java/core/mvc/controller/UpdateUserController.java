package core.mvc.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import next.controller.UserSessionUtils;
import next.model.User;

@Controller
public class UpdateUserController {

    private static final String NOT_OWNER_USER = "다른 사용자의 정보를 수정할 수 없습니다.";

    @RequestMapping(value = "/users/updateForm")
    public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp)
        throws Exception {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException(NOT_OWNER_USER);
        }
        ModelAndView modelAndView = new ModelAndView(new JspView("/user/updateForm.jsp"));
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = DataBase.findUserById(req.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException(NOT_OWNER_USER);
        }

        User updateUser = new User(req.getParameter("userId"), req.getParameter("password"),
            req.getParameter("name"),
            req.getParameter("email"));
        user.update(updateUser);

        return new ModelAndView(new JspView("redirect:/"));
    }

}
