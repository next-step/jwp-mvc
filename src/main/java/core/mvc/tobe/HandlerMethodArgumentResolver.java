package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.mvc.MethodParameter;
import core.mvc.utils.PathPatternMatcher;
import org.omg.IOP.CodecPackage.TypeMismatch;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static core.mvc.utils.PathPatternMatcher.toPathContainer;

public class HandlerMethodArgumentResolver {
    private ParameterNameDiscoverer nameDiscoverer;

    public HandlerMethodArgumentResolver() {
        this.nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    }

    public Object[] getMethodArguments(HttpServletRequest request, Method method) throws Exception {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        assert parameterNames != null;

        List<MethodParameter> methodParameters = new ArrayList<>();
        for (int i = 0; i < parameterNames.length; i++) {
            methodParameters.add(new MethodParameter(parameterNames[i], parameters[i]));
        }

        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        Map<String, String> pathVariables = PathPatternMatcher.parse(annotation.value())
                .matchAndExtract(toPathContainer(request.getRequestURI())).getUriVariables();

        int size = methodParameters.size();
        Object[] params = new Object[size];
        for (int i = 0; i < size; i++) {
            MethodParameter methodParameter = methodParameters.get(i);
            if (methodParameters.get(i).isPathVariable()) {
                params[i] = pathVariables.get(methodParameter.getName());
            } else {
                params[i] = getArgument(methodParameter, request);
            }
        }

        return params;
    }

    private Object getArgument(MethodParameter methodParameter, HttpServletRequest request) throws Exception {
        String value = request.getParameter(methodParameter.getName());

        if (methodParameter.getType().equals(HttpSession.class)) {
            return request.getSession();
        }

        if (methodParameter.getType().equals(int.class)) {
            return Integer.parseInt(value);
        }

        if (methodParameter.getType().equals(Integer.class)) {
            return Integer.parseInt(value);
        }

        if (methodParameter.getType().equals(long.class)) {
            return Long.parseLong(value);
        }

        if (methodParameter.getType().equals(Long.class)) {
            return Long.parseLong(value);
        }

        if (methodParameter.getType().equals(String.class)) {
            return value;
        }

        throw new Exception("Type TypeMismatch");
    }
}
