package core.mvc.tobe.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;

public class BeanTypeArgumentResolver extends AbstractArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		Class<?> parameterType = methodParameter.getParameterType();
		return CharSequence.class.isAssignableFrom(parameterType) && !BeanUtils.isSimpleProperty(parameterType);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
		Class<?> parameterType = methodParameter.getParameterType();
		try {
			return getBean(parameterType, request);
		} catch (IllegalStateException e) {
			throw new RuntimeException(methodParameter.getParameterType() + " Constructor access Failed");
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(methodParameter.getParameterType() +  " Invoke Fail");
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException(methodParameter.getParameterType() +  " Instantiation Fail");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(methodParameter.getParameterType() +  " Illegal Access Fail");
		}
	}

	private Object getBean(Class<?> parameterType, HttpServletRequest request) throws InvocationTargetException, InstantiationException, IllegalAccessException {
		for (Constructor constructor : parameterType.getDeclaredConstructors()) {
			String[] parameterNames = nameDiscoverer.getParameterNames(constructor);
			Class[] parameterTypes = constructor.getParameterTypes();

			return constructor.newInstance(getArguments(parameterNames, parameterTypes, request));
		}

		throw new IllegalStateException();
	}

	private Object[] getArguments(String[] parameterNames, Class[] parameterTypes, HttpServletRequest request) {
		Object[] args = new Object[parameterNames.length];

		for (int i = 0; i < parameterNames.length; i++) {
			args[i] = ParameterTypeUtils.cast(parameterTypes[i], request.getParameter(parameterNames[i]));
		}

		return args;
	}
}
