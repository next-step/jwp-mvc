package core.mvc.tobe.handler.resolver;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

public class ArgumentResolvers {

    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public Object[] resolveParameters(Method method, HttpServletRequest request, HttpServletResponse response) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

        Object[] arguments = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String parameterName = parameterNames[i];
            String value = request.getParameter(parameterName);

            if (HttpServletRequest.class.isAssignableFrom(parameter.getType())) {
                arguments[i] = request;

            } else if (HttpServletResponse.class.isAssignableFrom(parameter.getType())) {
                arguments[i] = response;
            } else if (Objects.nonNull(value) && isDefaultType(parameter)) {
                if (String.class.isAssignableFrom(parameter.getType())) {
                    arguments[i] = value;
                } else if (isIntOrLongType(parameter)) {
                    if (isIntOrInteger(parameter)) {
                        arguments[i] = Integer.parseInt(value);
                    } else if(isLong(parameter)) {
                        arguments[i] = Long.parseLong(value);
                    }
                }
            } else {
                throw new NoExistsArgumentResolverException("Controller 실행에 필요한 매개변수에 값을 할당할 argumentResolver가 존재하지 않습니다.");
            }
        }

        return arguments;
    }

    private boolean isDefaultType(Parameter parameter) {
        return String.class.isAssignableFrom(parameter.getType()) || isIntOrLongType(parameter);
    }

    private boolean isIntOrLongType(Parameter parameter) {
        return isIntOrInteger(parameter) || isLong(parameter);
    }

    private boolean isIntOrInteger(Parameter parameter) {
        return Integer.TYPE.isAssignableFrom(parameter.getType()) ||
                Integer.class.isAssignableFrom(parameter.getType());
    }

    private boolean isLong(Parameter parameter) {
        return Long.TYPE.isAssignableFrom(parameter.getType()) ||
                Long.class.isAssignableFrom(parameter.getType());
    }
}
