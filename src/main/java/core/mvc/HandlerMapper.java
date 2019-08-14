package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMapper {

    boolean isSupport(final HttpServletRequest request);
    ModelAndView mapping(final HttpServletRequest request, final HttpServletResponse response);
}
