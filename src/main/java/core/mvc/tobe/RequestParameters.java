package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestParameters {

    private final String[] parameterNames;
    private Class<?>[] parameterTypes;
    private Map<String, String> pathVariables;
    private List<Object> parameterList;

    public RequestParameters(HttpServletRequest request, Method method) {
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        parameterNames = nameDiscoverer.getParameterNames(method);
        if (parameterNames == null) {
            return;
        }
        pathVariables = pathVariables(request, method);
        parameterTypes = method.getParameterTypes();
        parameterList = new ArrayList<>();
    }

    public List<Object> extraction(HttpServletRequest request, HttpServletResponse response) {
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            Class<?> parameterType = parameterTypes[i];

            String parameter = request.getParameter(parameterName);
            if (parameter != null) {
                parameterList.add(ParameterTypeEnum.casting(parameter, parameterType));
                continue;
            }

            String pathVariable = pathVariables.keySet().stream().filter(key -> key.equals(parameterName))
                    .map(pathVariables::get).findFirst().orElse(null);
            if (pathVariable != null) {
                parameterList.add(ParameterTypeEnum.casting(pathVariable, parameterType));
                continue;
            }

            Object attribute = request.getAttribute(parameterName);
            if (attribute != null) {
                parameterList.add(attribute);
                continue;
            }

            if (parameterType.getSimpleName().equals("HttpServletRequest")) {
                parameterList.add(request);
                continue;
            }

            if (parameterType.getSimpleName().equals("HttpServletResponse")) {
                parameterList.add(response);
            }
        }
        return parameterList;
    }

    private Map<String, String> pathVariables(HttpServletRequest request, Method method) {
        RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
        UriPathPattern uriPathPattern = new UriPathPattern(requestMappingAnnotation.value());
        return uriPathPattern.matchAndExtract(request.getRequestURI());
    }
}
