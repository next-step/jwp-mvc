package core.mvc.resolver;

import core.annotation.web.RequestParam;
import core.mvc.resolver.util.ParameterUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class RequestParamArgumentResolver implements ArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return Arrays.stream(methodParameter.getParameterAnnotations())
                .anyMatch(annotation -> annotation.annotationType() == RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        String argumentName = new LocalVariableTableParameterNameDiscoverer().getParameterNames(methodParameter.getMethod())[methodParameter.getParameterIndex()];
        Object parameter = request.getParameter(argumentName);

        return ParameterUtils.getObjectByParameterType(parameter, methodParameter.getParameterType());
    }
}
