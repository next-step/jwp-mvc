package core.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ArgumentResolver {
    boolean supportsParameter(MethodParameter methodParameter);
    Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response);
}
