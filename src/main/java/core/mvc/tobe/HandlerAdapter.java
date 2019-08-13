package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean supports(HttpServletRequest req);

    ModelAndView handle(HttpServletRequest req, HttpServletResponse resp) throws Exception;

}
