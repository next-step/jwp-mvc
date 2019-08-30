package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParameterMapHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameters methodParameters, final HttpRequestParameters requestParameters) {
        return methodParameters.stream()
                .anyMatch(entry -> requestParameters.containsKey(entry.getKey()));
    }

    @Override
    public Object[] resolveArgument(final MethodParameters methodParameters,
                                    final HttpRequestParameters requestParameters,
                                    final HttpServletRequest request,
                                    final HttpServletResponse response) {

        return methodParameters.createArguments(requestParameters);
    }
}
