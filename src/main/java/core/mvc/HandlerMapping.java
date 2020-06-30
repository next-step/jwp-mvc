package core.mvc;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    Handler getHandler(HttpServletRequest request);
}
