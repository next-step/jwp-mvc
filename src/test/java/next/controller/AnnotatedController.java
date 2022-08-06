package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import core.mvc.tobe.JspView;

@Controller
public class AnnotatedController {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home() {
        final ModelAndView modelAndView = new ModelAndView(new JspView("/home.jsp"));
        modelAndView.addObject("message", "Hello World");

        return modelAndView;
    }
}
