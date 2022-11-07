package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.view.ForwardView;
import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("users", DataBase.findAll());

        ModelAndView modelAndView = new ModelAndView(new ForwardView("home.jsp"));
        modelAndView.addObject("users", DataBase.findAll());

        return modelAndView;
    }
}
