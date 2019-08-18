package core.mvc.tobe;

import core.mvc.MethodParameter;
import org.omg.IOP.CodecPackage.TypeMismatch;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class HandlerMethodArgumentResolver {
    private ParameterNameDiscoverer nameDiscoverer;

    public HandlerMethodArgumentResolver() {
        this.nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    }

    public Object[] getMethodArguments(HttpServletRequest request, Method method) throws TypeMismatch {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        assert parameterNames != null;

        List<MethodParameter> methodParameters = new ArrayList<>();
        for (int i = 0; i < parameterNames.length; i++) {
            methodParameters.add(new MethodParameter(parameterNames[i], parameters[i].getType()));
        }

        int size = methodParameters.size();
        Object[] params = new Object[size];
        for (int i = 0; i < size; i++) {
            params[i] = getArgument(methodParameters.get(i), request);
        }

        return params;
    }

    private Object getArgument(MethodParameter methodParameter, HttpServletRequest request) throws TypeMismatch {
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

        throw new TypeMismatch();
    }
}
