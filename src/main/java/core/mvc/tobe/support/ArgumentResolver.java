package core.mvc.tobe.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;

public interface ArgumentResolver {
	boolean supportsParameter(MethodParameter methodParameter);

	Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response);
}
