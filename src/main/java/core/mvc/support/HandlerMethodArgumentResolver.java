package core.mvc.support;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface to be implemented by objects that resolve arguments of method from HttpServletRequest.
 *
 * @author hyeyoom
 */
public interface HandlerMethodArgumentResolver {

    /**
     * @param methodParameter method parameter
     * @return true if it supports given parameter
     */
    boolean supportsParameter(MethodParameter methodParameter);

    /**
     * @param methodParameter method parameter
     * @param request http request
     * @return resolved value
     */
    Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request);
}
