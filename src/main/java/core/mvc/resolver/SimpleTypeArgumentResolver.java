package core.mvc.resolver;

import core.mvc.resolver.util.ParameterUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleTypeArgumentResolver implements ArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return BeanUtils.isSimpleProperty(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        String argumentName = new LocalVariableTableParameterNameDiscoverer().getParameterNames(methodParameter.getMethod())[methodParameter.getParameterIndex()];
        Object parameter = request.getParameter(argumentName);

        return ParameterUtils.getObjectByParameterType(parameter, methodParameter.getParameterType());
    }
}
