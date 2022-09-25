package core.mvc.tobe.resolver;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimplePropertyArgumentResolver extends AbstractArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return BeanUtils.isSimpleProperty(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        String argumentName = getArgumentName(methodParameter.getMethod(), methodParameter.getParameterIndex());
        Object parameter = request.getParameter(argumentName);

        return TypeConverter.cast(methodParameter.getParameterType(), parameter);
    }
}
