package core.mvc;

import javax.servlet.http.HttpServletRequest;

/**
 * Created By kjs4395 on 2020-06-19
 */
public interface HandlerMapping {
    void initialize();

    Object getHandler(HttpServletRequest request);
}
