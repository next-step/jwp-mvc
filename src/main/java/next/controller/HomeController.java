package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView execute() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", DataBase.findAll());
        mav.setView("home.jsp");
        return mav;
    }
}
