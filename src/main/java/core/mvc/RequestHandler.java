package core.mvc;

import javax.servlet.http.HttpServletRequest;

public interface RequestHandler {
    Object getHandler(HttpServletRequest request);
}
