package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.function.Function;

@Controller
public class LoginController {

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView execute(final HttpServletRequest request,
                                final HttpServletResponse response) {
        final String userId = request.getParameter("userId");
        final String password = request.getParameter("password");

        return Optional.ofNullable(DataBase.findUserById(userId))
                .filter(user -> user.matchPassword(password))
                .map(successHandle(request))
                .orElseGet(this::failHandle);
    }

    private Function<User, ModelAndView> successHandle(final HttpServletRequest request) {
        return user -> {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);

            return new ModelAndView(new JspView("redirect:/"));
        };
    }

    private ModelAndView failHandle() {
        final ModelAndView mvn = new ModelAndView(new JspView("/user/login.jsp"));
        mvn.addObject("loginFailed", true);
        return mvn;
    }
}
