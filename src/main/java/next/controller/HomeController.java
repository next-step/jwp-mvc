package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("users", DataBase.findAll());
        return "home.jsp";
    }
}
