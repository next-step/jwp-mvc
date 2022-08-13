package core.mvc.tobe.handler.resolver;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class ArgumentResolvers {

    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private final List<ArgumentResiolver> argumentResolvers = List.of(
            new HttpServletArgumentResolver(),
            new StringTypeRequestParameterArgumentResolver(),
            new IntegerTypeRequestParameterArgumentResolver(),
            new LongTypeRequestParameterArgumentResolver()
    );

    public Object[] resolveParameters(Method method, HttpServletRequest request, HttpServletResponse response) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

        Object[] arguments = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String parameterName = parameterNames[i];
            String value = request.getParameter(parameterName);

            NamedParameter namedParameter = new NamedParameter(parameter, parameterName);

            Object argument = argumentResolvers.stream()
                    .filter(resolver -> resolver.support(namedParameter))
                    .map(resolver -> resolver.resolve(namedParameter, request, response))
                    .findFirst()
                    .orElseThrow(() -> new NoExistsArgumentResolverException("Controller 실행에 필요한 매개변수에 값을 할당할 argumentResolver가 존재하지 않습니다."));

            arguments[i] = argument;
        }

        return arguments;
    }
}
