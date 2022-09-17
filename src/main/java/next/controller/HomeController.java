package next.controller;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ForwardView;
import core.mvc.ModelAndView;
import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@core.annotation.web.Controller
public class HomeController implements Controller {

    @Override
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ModelAndView mv = new ModelAndView(new ForwardView("home.jsp"));
        mv.addObject("users", DataBase.findAll());
        return mv;
    }
}
