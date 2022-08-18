package core.mvc.tobe.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;

import core.annotation.web.RequestParam;
import core.mvc.tobe.support.utils.ParameterTypeUtils;

public class RequestParamArgumentResolver extends AbstractAnnotationArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return supportAnnotation(methodParameter, RequestParam.class);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
		String argumentName = getArgumentName(methodParameter.getMethod(), methodParameter.getParameterIndex());
		Object parameter = request.getParameter(argumentName);

		return ParameterTypeUtils.cast(methodParameter.getParameterType(), parameter);
	}
}
