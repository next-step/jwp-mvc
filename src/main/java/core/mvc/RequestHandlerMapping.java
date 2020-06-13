package core.mvc;

import javax.servlet.http.HttpServletRequest;

public interface RequestHandlerMapping {
    Object getHandler(HttpServletRequest request);
}
