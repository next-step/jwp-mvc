package core.mvc.tobe.argumentresolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

public interface MethodArgumentResolver {
    boolean support(MethodParameter methodParameter);

    Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException, InstantiationException;
}
