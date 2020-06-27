package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.view.JspView;
import core.mvc.ModelAndView;
import core.mvc.view.RedirectView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import next.model.User;

@Controller("/users")
public class UserController {

    @RequestMapping("")
    public ModelAndView users(HttpSession httpSession){

        if (!UserSessionUtils.isLogined(httpSession)) {
            return new ModelAndView(new RedirectView("/users/loginForm"));
        }
        ModelAndView modelAndView = new ModelAndView(new JspView("/user/list.jsp"));
        modelAndView.addObject("users", DataBase.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(User user){
        DataBase.addUser(user);
        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(HttpSession httpSession, User user){
        if (!UserSessionUtils.isSameUser(httpSession, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        user.update(user);
        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/updateForm")
    public ModelAndView updateForm(HttpSession httpSession, String userId){
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(httpSession, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        ModelAndView modelAndView = new ModelAndView(new JspView("/user/updateForm.jsp"));
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/profile")
    public ModelAndView profile(String userId){
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        ModelAndView modelAndView = new ModelAndView(new JspView("/user/profile.jsp"));
        modelAndView.addObject("user", user);
        return modelAndView;
    }

}
