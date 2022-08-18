package core.mvc.tobe.support;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;

import core.annotation.web.RequestParam;

public class RequestParamArgumentResolver extends AbstractAnnotationArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return Arrays.stream(methodParameter.getParameterAnnotations())
					 .anyMatch(annotation -> annotation.annotationType().equals(RequestParam.class));
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
		String argumentName = getArgumentName(methodParameter.getMethod(), methodParameter.getParameterIndex());
		Object parameter = request.getParameter(argumentName);

		if (methodParameter.getParameterType().equals(String.class)) {
			return parameter;
		}
		if (methodParameter.getParameterType().equals(Integer.class)) {
			return Integer.valueOf(parameter.toString());
		}
		if (methodParameter.getParameterType().equals(int.class)) {
			return Integer.parseInt(parameter.toString());
		}
		if (methodParameter.getParameterType().equals(long.class)) {
			return Long.parseLong(parameter.toString());
		}

		return new RuntimeException("Not Support Argument");
	}
}
