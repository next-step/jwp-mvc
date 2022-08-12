package core.mvc.tobe.handler.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ArgumentResolvers {

    public Object[] resolveParameters(Method method, HttpServletRequest request, HttpServletResponse response) {
        Parameter[] parameters = method.getParameters();
        Object[] arguments = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (HttpServletRequest.class.isAssignableFrom(parameter.getType())) {
                arguments[i] = request;

            } else if (HttpServletResponse.class.isAssignableFrom(parameter.getType())) {
                arguments[i] = response;
            } else {
                throw new NoExistsArgumentResolverException("Controller 실행에 필요한 매개변수에 값을 할당할 argumentResolver가 존재하지 않습니다.");
            }
        }

        return arguments;
    }
}
