package core.mvc.exception;

import core.mvc.ModelAndView;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExceptionResolver {
    ModelAndView resolveException(ServletException ex, HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
