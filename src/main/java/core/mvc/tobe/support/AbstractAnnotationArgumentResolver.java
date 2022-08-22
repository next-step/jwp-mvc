package core.mvc.tobe.support;

import java.util.Arrays;

import org.springframework.core.MethodParameter;

public abstract class AbstractAnnotationArgumentResolver extends AbstractArgumentResolver {
	protected boolean supportAnnotation(MethodParameter methodParameter, Class<?> declareAnnotation) {
		return Arrays.stream(methodParameter.getParameterAnnotations())
					 .anyMatch(annotation -> annotation.annotationType().equals(declareAnnotation));
	}
}
