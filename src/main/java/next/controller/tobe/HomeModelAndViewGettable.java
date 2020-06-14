package next.controller.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.ModelAndViewGettable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static core.annotation.web.RequestMethod.GET;

@Controller
public class HomeModelAndViewGettable implements ModelAndViewGettable {

    @RequestMapping(value = "/", method = GET)
    public ModelAndView home(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new JspView("home.jsp"));
    }
}
