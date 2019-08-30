package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServletRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameters parameter, final HttpRequestParameters requestParameters) {
        return parameter.isParameterTypePresent(HttpServletRequest.class);
    }

    @Override
    public Object[] resolveArgument(final MethodParameters methodParameters,
                                    final HttpRequestParameters requestParameters,
                                    final HttpServletRequest request,
                                    final HttpServletResponse response) {
        return new Object[] { request, response };
    }
}
