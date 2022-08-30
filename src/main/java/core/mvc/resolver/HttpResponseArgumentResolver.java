package core.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpResponseArgumentResolver implements ArgumentResolver {
    @Override
    public boolean supports(RequestParameter requestParameter) {
        return requestParameter.getClassType() == HttpServletResponse.class;
    }

    @Override
    public Object resolveArgument(RequestParameter requestParameter, HttpServletRequest request, HttpServletResponse response) {
        return response;
    }
}
