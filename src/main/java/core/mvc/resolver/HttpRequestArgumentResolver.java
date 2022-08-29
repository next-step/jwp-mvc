package core.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpRequestArgumentResolver implements ArgumentResolver {
    @Override
    public boolean supports(RequestParameter requestParameter) {
        return requestParameter.getClassType() == HttpServletRequest.class;
    }

    @Override
    public Object resolveArgument(RequestParameter requestParameter, HttpServletRequest request, HttpServletResponse response) {
        return request;
    }
}
