package core.mvc.tobe.resolver;

import core.mvc.tobe.MethodParameter;
import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimplePropertyArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return BeanUtils.isSimpleProperty(methodParameter.getType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        String argumentName = methodParameter.getParameterName();
        Object parameter = request.getParameter(argumentName);

        return TypeConverter.cast(methodParameter.getType(), parameter);
    }

}
