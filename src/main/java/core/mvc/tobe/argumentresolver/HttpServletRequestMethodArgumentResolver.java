package core.mvc.tobe.argumentresolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean resolvable(final Method method, final Parameter parameter) {
        if (parameter == null) {
            return false;
        }
        return HttpServletRequest.class == parameter.getType();
    }

    @Override
    public Object resolve(final Method method, final Parameter parameter, final String parameterName, final HttpServletRequest request) {
        return request;
    }

}
