package core.mvc.tobe.argumentresolver;

import java.lang.reflect.Array;
import javax.servlet.http.HttpServletRequest;

public interface MethodArgumentResolver {

    boolean resolvable(MethodParameter methodParameter);

    Object resolve(MethodParameter methodParameter, HttpServletRequest request);

    default Object getDefaultValue(Class<?> clazz) {
        return Array.get(Array.newInstance(clazz, 1), 0);
    }
}
