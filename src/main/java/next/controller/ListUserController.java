package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static core.annotation.web.RequestMethod.GET;

@Controller
public class ListUserController {
    @RequestMapping(value = "/users", method = GET)
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return "redirect:/users/loginForm";
        }

        req.setAttribute("users", DataBase.findAll());
        return "/user/list.jsp";
    }
}
