package core.mvc.tobe.argumentresolver;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

public interface HandlerMethodArgumentResolver {
    Object resolveArgument(HttpServletRequest request, Parameter parameter, String parameterName);
    boolean supportsParameter(Parameter parameter);
}
