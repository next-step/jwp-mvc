package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodArgumentResolver {
    boolean supportsParameter(final MethodParameters methodParameters, final HttpRequestParameters requestParameters);
    Object[] resolveArgument(final MethodParameters methodParameters,
                             final HttpRequestParameters requestParameters,
                             final HttpServletRequest request,
                             final HttpServletResponse response);
}
