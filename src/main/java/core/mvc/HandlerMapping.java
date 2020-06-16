package core.mvc;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface to be implemented by objects that define sort of relation between requests and handlers.
 *
 * @author hyeyoom
 */
public interface HandlerMapping {

    /**
     * @param request servlet request
     * @return mapped handler
     */
    Object getHandler(HttpServletRequest request);
}
