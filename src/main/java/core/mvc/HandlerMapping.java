package core.mvc;

import javax.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface HandlerMapping {
    Object getHandler(HttpServletRequest request);
}
