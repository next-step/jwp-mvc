package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

import static core.annotation.web.RequestMethod.GET;

@Slf4j
@Controller
public class HomeController {

    @RequestMapping(value = "/", method = GET)
    public ModelAndView getMainPage(HttpServletRequest request) {
        request.setAttribute("users", DataBase.findAll());

        return new ModelAndView("home.jsp");
    }

    @RequestMapping(value = "/{id}", method = GET)
    public ModelAndView getMainPage(@PathVariable long id, HttpServletRequest request) {
        log.info("id={}", id);
        request.setAttribute("users", DataBase.findAll());

        return new ModelAndView("home.jsp");
    }
}
