package core.mvc.asis;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.ModelAndViewHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller extends ModelAndViewHandler {
    String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception;


    default ModelAndView handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String path = execute(req, resp);
        return new ModelAndView(new JspView(path));
    }
}
