package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Handler {

    ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
