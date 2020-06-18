package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author KingCjy
 */
public interface HandlerExecutor {
    boolean supportHandler(Object object);
    ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception;
}
