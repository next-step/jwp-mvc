package core.mvc.tobe;

import core.annotation.web.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static core.mvc.tobe.ParameterUtils.decideParameter;
import static core.mvc.tobe.ParameterUtils.isPathVariable;
import static core.mvc.tobe.ParameterUtils.parsePath;
import static core.mvc.tobe.ParameterUtils.toPathContainer;

public class PathVariableArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean support(Method method) {
        return isPathVariable(method);
    }

    @Override
    public Object[] resolve(Method method, HttpServletRequest request, HttpServletResponse response) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        Map<String, String> variables = parsePath(annotation.value())
                .matchAndExtract(toPathContainer(request.getRequestURI())).getUriVariables();
        List<String> variableValues = new ArrayList<>(variables.values());

        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] values = new Object[variables.size()];
        for (int i = 0; i < variableValues.size(); i++) {
            Class<?> parameterType = parameterTypes[i];
            values[i] = decideParameter(variableValues.get(i), parameterType);
        }
        return values;
    }

}
