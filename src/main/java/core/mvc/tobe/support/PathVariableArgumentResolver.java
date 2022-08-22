package core.mvc.tobe.support;

import java.lang.reflect.Method;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.support.utils.ParameterTypeUtils;
import core.mvc.tobe.support.utils.PathPatternUtils;

public class PathVariableArgumentResolver extends AbstractAnnotationArgumentResolver {

	private static final String NOT_SUPPORTED_ANNOTATION = "Not Supported RequestMapping Annotation";
	private static final String NOT_EXTRACT_ARGUMENT = "Can't Extract PathVariable Value";

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return supportAnnotation(methodParameter, PathVariable.class);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
		String pattern = getPattern(methodParameter.getMethod());
		String uri = request.getRequestURI();
		String argumentName = getArgumentName(methodParameter.getMethod(), methodParameter.getParameterIndex());

		String value = PathPatternUtils.getValue(pattern, uri, argumentName);
		if (Objects.isNull(value)) {
			throw new RuntimeException(NOT_EXTRACT_ARGUMENT);
		}
		return ParameterTypeUtils.cast(methodParameter.getParameterType(), value);
	}

	private String getPattern(Method method) {
		if (method.isAnnotationPresent(RequestMapping.class)) {
			RequestMapping annotation = method.getAnnotation(RequestMapping.class);
			return annotation.value();
		}
		throw new RuntimeException(NOT_SUPPORTED_ANNOTATION);
	}

}
