package core.mvc.tobe.argumentresolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public class HttpServletObjectArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(HttpServletRequest request, HttpServletResponse response, Parameter parameter, String parameterName) {
        if (parameter.getType().equals(HttpServletRequest.class)) {
            return request;
        }

        return response;
    }

    @Override
    public boolean supportsParameter(Parameter parameter) {
        return parameter.getType().equals(HttpServletRequest.class) ||
                parameter.getType().equals(HttpServletResponse.class);
    }
}
