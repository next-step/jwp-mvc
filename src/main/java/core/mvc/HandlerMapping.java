package core.mvc;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : yusik
 * @date : 2019-08-15
 */
public interface HandlerMapping {
    Object getHandler(HttpServletRequest request);
}
