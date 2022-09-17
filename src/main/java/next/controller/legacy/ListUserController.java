package next.controller.legacy;

import core.db.DataBase;
import core.mvc.ForwardView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import core.mvc.asis.Controller;
import next.controller.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListUserController implements Controller {

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return new ModelAndView(new RedirectView("redirect:/users/loginForm"));
        }

        ModelAndView mv = new ModelAndView(new ForwardView("/user/list.jsp"));
        mv.addObject("users", DataBase.findAll());
        return mv;
    }
}
