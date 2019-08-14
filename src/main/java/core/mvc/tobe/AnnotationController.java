package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AnnotationController {
    ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
