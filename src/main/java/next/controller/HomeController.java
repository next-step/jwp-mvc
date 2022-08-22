package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.view.ForwardView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new ForwardView("home.jsp"));
    }
}
