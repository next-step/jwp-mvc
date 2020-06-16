package core.mvc;

import core.mvc.exceptions.UnableToResolveException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface to be implemented by objects that map view name to actual views.
 * throws UnableToResolveException if view does not exist!
 *
 * @author hyeyoom
 */
public interface ViewResolver {
    ModelAndView resolve(HttpServletRequest request, HttpServletResponse response) throws UnableToResolveException;
}
