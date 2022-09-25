package core.mvc.tobe.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HttpRequestArgumentResolver implements ArgumentResolver {

    private static final HttpRequestArgumentResolver httpRequestArgumentResolver = new HttpRequestArgumentResolver();

    private HttpRequestArgumentResolver() {}

    public static HttpRequestArgumentResolver getInstance() {
        return httpRequestArgumentResolver;
    }

    @Override
    public Object[] resolve(HttpServletRequest request, HttpServletResponse response, Method method) {
        return Arrays.stream(method.getParameterTypes())
                .filter(parameterType -> parameterType.equals(HttpServletRequest.class))
                .map(parameterType -> request).toArray();
    }

}
