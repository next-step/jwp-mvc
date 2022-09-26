package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.tobe.ForwardView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserFormController {

    private static final Logger logger = LoggerFactory.getLogger(UserFormController.class);


    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView usersForm() {
        return new ModelAndView(new ForwardView("/user/form.jsp"));
    }

    @RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
    public ModelAndView usersLoginForm() {
        return new ModelAndView(new ForwardView("/user/login.jsp"));
    }

    @RequestMapping(value = "/users/updateForm", method = RequestMethod.GET)
    public ModelAndView userUpdateForm(HttpServletRequest request) {
        User user = DataBase.findUserById(request.getParameter("userId"));

        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        request.setAttribute("user", user);
        return new ModelAndView(new ForwardView("/user/updateForm.jsp"));
    }
}
