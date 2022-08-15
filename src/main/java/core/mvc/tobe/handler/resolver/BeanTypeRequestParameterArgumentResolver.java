package core.mvc.tobe.handler.resolver;

import core.mvc.tobe.handler.TargetHandlingException;
import core.mvc.tobe.handler.resolver.utils.TypeUtils;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;

public class BeanTypeRequestParameterArgumentResolver implements ArgumentResolver {

    private final ParameterNameDiscoverer parameterNameDiscoverer;
    private final SimpleTypeRequestParameterArgumentResolver simpleTypeRequestParameterArgumentResolver;

    public BeanTypeRequestParameterArgumentResolver(ParameterNameDiscoverer parameterNameDiscoverer, SimpleTypeRequestParameterArgumentResolver simpleTypeRequestParameterArgumentResolver) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
        this.simpleTypeRequestParameterArgumentResolver = simpleTypeRequestParameterArgumentResolver;
    }

    @Override
    public boolean support(NamedParameter parameter) {
        return hasSingleConstructor(parameter) && isConstructorAllSimpleTypeArguments(parameter);
    }

    private boolean hasSingleConstructor(NamedParameter parameter) {
        Class<?> type = parameter.getType();
        Constructor<?>[] declaredConstructors = type.getDeclaredConstructors();
        return declaredConstructors.length == 1;
    }

    private boolean isConstructorAllSimpleTypeArguments(NamedParameter parameter) {
        Constructor<?> constructor = getConstructor(parameter);

        return Arrays.stream(constructor.getParameters())
                .map(Parameter::getType)
                .allMatch(type -> TypeUtils.isSimpleType(type));
    }

    private Constructor<?> getConstructor(NamedParameter parameter) {
        Class<?> type = parameter.getType();
        Constructor<?>[] declaredConstructors = type.getDeclaredConstructors();
        Constructor<?> constructor = declaredConstructors[0];
        return constructor;
    }

    @Override
    public Object resolve(NamedParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        try {
            Constructor<?> constructor = getConstructor(parameter);
            Object[] arguments = resolveParameters(constructor, request, response);
            constructor.setAccessible(true);
            return constructor.newInstance(arguments);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            Throwable rootCauseException = e.getCause();
            if (Objects.nonNull(rootCauseException)) {
                throw new TargetHandlingException(rootCauseException.getMessage(), rootCauseException);
            }
            throw new Error(e);
        }
    }

    private Object[] resolveParameters(Constructor<?> constructor, HttpServletRequest request, HttpServletResponse response) {
        Parameter[] parameters = constructor.getParameters();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(constructor);
        return getObjects(request, response, parameters, parameterNames);
    }

    private Object[] getObjects(HttpServletRequest request, HttpServletResponse response, Parameter[] parameters, String[] parameterNames) {
        Object[] arguments = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter innerParameter = parameters[i];
            String parameterName = parameterNames[i];
            NamedParameter namedParameter = new NamedParameter(innerParameter, parameterName);
            Object argument = simpleTypeRequestParameterArgumentResolver.resolve(namedParameter, request, response);

            arguments[i] = argument;
        }
        return arguments;
    }
}
