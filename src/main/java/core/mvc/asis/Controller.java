package core.mvc.asis;

import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
    ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
