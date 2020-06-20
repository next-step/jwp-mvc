package core.mvc.support;

import javax.servlet.http.HttpServletRequest;

/**
 * See link below:
 * {@link core.annotation.web.PathVariable}
 *
 * @author hyeyoom
 */
public class PathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) {
        return null;
    }
}
