package core.mvc.supporter;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class PrimitiveTypeMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public boolean supportsParameter(Parameter parameter) {
        Class<?> type = parameter.getType();
        return type.isPrimitive() || String.class.equals(type);
    }

    @Override
    public Object resolveArgument(Parameter parameter, Method method,
                                  HttpServletRequest httpServletRequest, int index) {
        String value = httpServletRequest.getParameter(nameDiscoverer.getParameterNames(method)[index]);
        return TypeConverter.convert(parameter.getType(), value);
    }
}
