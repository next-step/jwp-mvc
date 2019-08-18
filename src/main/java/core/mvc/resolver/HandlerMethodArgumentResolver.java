package core.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

public class HandlerMethodArgumentResolver {
    private static final ParameterInstanceCreator PARAMETER_INSTANCE_CREATOR = new ParameterInstanceCreator();

    public static Object[] resolve(Method method, HttpServletRequest request, HttpServletResponse response) {
        MethodParameter methodParameter = MethodParameter.from(method, request.getRequestURI());
        List<String> parameterNames = methodParameter.getParameterNames();

        return parameterNames.stream()
                .map(name -> resolveParameter(methodParameter.getType(name), request, response, methodParameter.getParameterValue(request, name)))
                .toArray();
    }

    private static <T> T resolveParameter(Class<T> type, HttpServletRequest request, HttpServletResponse response, String parameterValue) {
        if (isHttpServletRequest(type)) {
            return (T) request;
        }

        if (isHttpServletResponse(type)) {
            return (T) response;
        }

        return TypeConverter.convert(type, parameterValue)
                .orElseGet(() -> PARAMETER_INSTANCE_CREATOR.create(type, request));
    }

    private static boolean isHttpServletResponse(Class<?> type) {
        return type == HttpServletResponse.class;
    }

    private static boolean isHttpServletRequest(Class<?> type) {
        return type == HttpServletRequest.class;
    }

}
