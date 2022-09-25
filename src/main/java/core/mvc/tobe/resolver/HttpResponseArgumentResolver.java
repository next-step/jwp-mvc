package core.mvc.tobe.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HttpResponseArgumentResolver implements ArgumentResolver {

    private static final HttpResponseArgumentResolver httpResponseArgumentResolver = new HttpResponseArgumentResolver();

    private HttpResponseArgumentResolver() {}

    public static HttpResponseArgumentResolver getInstance() {
        return httpResponseArgumentResolver;
    }

    @Override
    public Object[] resolve(HttpServletRequest request, HttpServletResponse response, Method method) {
        return Arrays.stream(method.getParameterTypes())
                .filter(parameterType -> parameterType.equals(HttpServletResponse.class))
                .map(parameterType -> response).toArray();
    }

}
