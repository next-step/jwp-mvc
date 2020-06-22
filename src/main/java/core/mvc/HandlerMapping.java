package core.mvc;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by iltaek on 2020/06/19 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public interface HandlerMapping {

    Object getHandler(HttpServletRequest request);
}
