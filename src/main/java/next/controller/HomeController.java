package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class HomeController implements core.mvc.asis.Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", DataBase.findAll());
        return "home.jsp";
    }

    @RequestMapping("/")
    public ModelAndView home(HttpServletRequest req, HttpServletResponse resp) {
        return ModelAndView.of(Map.of("users", DataBase.findAll()), JspView.from("home.jsp"));
    }
}
