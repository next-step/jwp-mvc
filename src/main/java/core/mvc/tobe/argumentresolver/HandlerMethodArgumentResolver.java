package core.mvc.tobe.argumentresolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public interface HandlerMethodArgumentResolver {
    Object resolveArgument(HttpServletRequest request, HttpServletResponse response, Parameter parameter, String parameterName);

    boolean supportsParameter(Parameter parameter);
}
