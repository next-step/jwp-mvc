package core.resolver;

import core.di.factory.ClassNewInstanceUtils;
import core.di.factory.ConstructorParameters;
import core.di.factory.MethodParameter;
import core.di.factory.ParameterTypeName;
import core.handler.converter.StringConverter;
import core.handler.converter.StringConverters;
import core.mvc.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParamClassTypeArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
    	return true;
    }

    @Override
    public Object resolve(MethodParameter methodParameter, WebRequest webRequest) {
        Class<?> type = methodParameter.getType();
        List<String> parameterNames = new ArrayList<>(webRequest.getRequest().getParameterMap().keySet());
        Optional<ConstructorParameters> consturctorParameters = ClassNewInstanceUtils.getContructorParameters(type, parameterNames);
        
        if(!consturctorParameters.isPresent()) {
        	return null;
        }
        
        return getConstructorInsatance(consturctorParameters.get(), webRequest);
    }
    
    private Object getConstructorInsatance(ConstructorParameters constructorParameters, WebRequest webRequest) {

    	Constructor<?> constructor = constructorParameters.getConstructor();
		Object[] args = getArgsByConstructor(constructorParameters, webRequest, constructor);

		try {
			return constructor.newInstance(args);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}
    }

	private Object[] getArgsByConstructor(ConstructorParameters constructorParameters, WebRequest webRequest, Constructor<?> constructor) {
		Object[] args = new Object[constructor.getParameterTypes().length];

		for(int i = 0; i < constructorParameters.getParameterTypeNames().size(); i ++) {
			ParameterTypeName parameterTypeName = constructorParameters.getParameterTypeNames().get(i);

			Class<?> type = parameterTypeName.getType();
			String parameterName = parameterTypeName.getName();
			HttpServletRequest request = webRequest.getRequest();
			StringConverter converter = StringConverters.getInstance().getConverter(type);
			args[i] = Optional.ofNullable(converter)
					.map(conv -> conv.convert(request.getParameter(parameterName)))
					.orElse(null);
		}
		return args;
	}
}
