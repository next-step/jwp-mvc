package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServletRequestArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.matches(HttpServletRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return request;
    }
}
