package core.mvc.asis;

import core.mvc.Handler;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Controller extends Handler {
    ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
