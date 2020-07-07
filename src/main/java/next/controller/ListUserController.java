package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ListUserController {

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView execute(HttpSession httpSession) throws Exception {
        ModelAndView mav = new ModelAndView();
        if (!UserSessionUtils.isLogined(httpSession)) {
            mav.setView("redirect:/users/loginForm");
            return mav;
        }

        mav.addObject("users", DataBase.findAll());
        mav.setView("/user/list.jsp");
        return mav;
    }
}
