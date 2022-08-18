package core.mvc.tobe.support;

import java.lang.reflect.Method;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

public abstract class AbstractAnnotationArgumentResolver implements ArgumentResolver {
	private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

	public String getArgumentName(Method method, int index) {
		return nameDiscoverer.getParameterNames(method)[index];
	}
}
