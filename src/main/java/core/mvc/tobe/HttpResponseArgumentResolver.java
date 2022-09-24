package core.mvc.tobe;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpResponseArgumentResolver implements ArgumentResolver {

    private final HttpServletResponse response;
    private final Class<?>[] parameterTypes;
    public HttpResponseArgumentResolver(HttpServletResponse response, Method method) {
        this.response = response;
        this.parameterTypes = method.getParameterTypes();
    }

    @Override
    public Object[] resolve() {
        return Arrays.stream(parameterTypes)
                .filter(parameterType -> parameterType.equals(HttpServletResponse.class))
                .map(parameterType -> response).toArray();
    }

}
