package core.mvc.view;

import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by iltaek on 2020/06/19 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public interface ViewResolver {

    ModelAndView resolve(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
