package core.mvc.tobe.requestAdapter;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created By kjs4395 on 2020-06-25
 */
public interface HandlerAdapter {
    boolean isSupport(Object executor);

    ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object executor) throws Exception;
}
