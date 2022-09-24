package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HttpRequestArgumentResolver implements ArgumentResolver {
    private final HttpServletRequest request;
    private final Class<?>[] parameterTypes;

    public HttpRequestArgumentResolver(HttpServletRequest request, Method method) {
        this.request = request;
        this.parameterTypes = method.getParameterTypes();
    }

    @Override
    public Object[] resolve() {
        return Arrays.stream(parameterTypes)
                .filter(parameterType -> parameterType.equals(HttpServletRequest.class))
                .map(parameterType -> request).toArray();
    }

}
