package core.mvc.tobe.argumentresolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;

public interface MethodArgumentResolver {

    boolean resolvable(Method method, Parameter parameter);

    Object resolve(Method method, HttpServletRequest request);
}
