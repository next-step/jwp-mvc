package core.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface to be implemented by objects that map view name to actual views.
 *
 * @author hyeyoom
 */
public interface ViewResolver {
    ModelAndView resolve(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
