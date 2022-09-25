package core.mvc.tobe.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public interface ArgumentResolver {
    Object[] resolve(HttpServletRequest request, HttpServletResponse response, Method method);
}
