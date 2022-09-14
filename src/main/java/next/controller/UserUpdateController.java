package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.tobe.ForwardView;
import core.mvc.tobe.RedirectView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller(path = "/users")
public class UserUpdateController {
    private static final Logger logger = LoggerFactory.getLogger(UserUpdateController.class);

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Request Path : {}", request.getRequestURI());

        User user = DataBase.findUserById(request.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(request.getParameter("userId"), request.getParameter("password"),
                request.getParameter("name"), request.getParameter("email"));
        user.update(updateUser);
        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/updateForm", method = RequestMethod.GET)
    public ModelAndView userUpdateForm(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Request Path : {}", request.getRequestURI());

        User user = DataBase.findUserById(request.getParameter("userId"));

        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        request.setAttribute("user", user);
        return new ModelAndView(new ForwardView("/user/updateForm.jsp"));
    }
}
