package core.mvc.tobe.resolver;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface ArgumentResolver {

    boolean supportsParameter(Parameter parameter);

    Object resolve(HttpServletRequest request, Method method, Parameter parameter, int index);
}
