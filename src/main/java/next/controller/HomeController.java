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

    @RequestMapping(value = "/{id}/users/{order}", method = GET)
    public ModelAndView testPathVariable(@PathVariable long id, @PathVariable Long order, HttpServletRequest request) {
        log.info("id={}, order={}", id, order);
        request.setAttribute("users", DataBase.findAll());

        return new ModelAndView("redirect:/");
    }
}
