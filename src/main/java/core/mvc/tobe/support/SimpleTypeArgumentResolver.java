package core.mvc.tobe.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;

public class SimpleTypeArgumentResolver extends AbstractArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return BeanUtils.isSimpleProperty(methodParameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
		String argumentName = getArgumentName(methodParameter.getMethod(), methodParameter.getParameterIndex());
		Object parameter = request.getParameter(argumentName);

		return ParameterTypeUtils.cast(methodParameter.getParameterType(), parameter);
	}
}
