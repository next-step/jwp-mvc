package core.mvc.tobe.resolver.argument;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class PathVariableArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean isSupport(Class<?> parameterType, Parameter parameter) {
        return parameter.isAnnotationPresent(PathVariable.class);
    }

    @Override
    public Object resolve(HttpServletRequest request, Method method, String parameterName, Class<?> parameterType) {
        String uri = method.getAnnotation(RequestMapping.class).value();
        int startIndex = uri.indexOf("{" + parameterName + "}");
        int endIndex = request.getRequestURI().indexOf("/", startIndex);

        String str = request.getRequestURI().substring(startIndex);
        if (endIndex >= 0) {
            str = request.getRequestURI().substring(startIndex, endIndex);
        }

        Object value = str;
        if (parameterType == int.class) {
            value = Integer.parseInt(str);
        }
        if (parameterType == long.class) {
            value = Long.parseLong(str);
        }

        return value;
    }
}
