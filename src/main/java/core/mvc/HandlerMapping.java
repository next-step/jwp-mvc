package core.mvc;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    void init();

    Object getHandler(HttpServletRequest request);
}
