package next.controller.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static core.annotation.web.RequestMethod.GET;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = GET)
    public String home(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", DataBase.findAll());
        return "home.jsp";
    }
}
