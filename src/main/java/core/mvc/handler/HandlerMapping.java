package core.mvc.handler;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    Object getHandler(HttpServletRequest request);
}
