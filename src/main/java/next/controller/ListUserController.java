package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ListUserController {
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return new ModelAndView("redirect:/users/loginForm");
        }

        return new ModelAndView("/user/list.jsp")
                .addObject("users", DataBase.findAll());
    }
}
