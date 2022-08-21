package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    Handler getHandler(HttpServletRequest request);
}
