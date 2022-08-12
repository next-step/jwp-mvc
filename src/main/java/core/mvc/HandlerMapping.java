package core.mvc;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    boolean isSupported(HttpServletRequest request);

    Object getHandler(HttpServletRequest request);
}
