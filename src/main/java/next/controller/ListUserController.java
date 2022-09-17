package next.controller;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ForwardView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@core.annotation.web.Controller
public class ListUserController implements Controller {

    @Override
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return new ModelAndView(new RedirectView("redirect:/users/loginForm"));
        }

        ModelAndView mv = new ModelAndView(new ForwardView("/user/list.jsp"));
        mv.addObject("users", DataBase.findAll());
        return mv;
    }
}
