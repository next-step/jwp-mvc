package core.mvc.tobe;

import core.mvc.JspView;
import core.mvc.ModelAndView;

public class AbstractController {
    public ModelAndView jspView(String url) {
        return new ModelAndView(new JspView(url));
    }
}
