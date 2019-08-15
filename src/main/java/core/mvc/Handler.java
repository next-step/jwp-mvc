package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response);

    boolean isSupport(HttpServletRequest req);
}
