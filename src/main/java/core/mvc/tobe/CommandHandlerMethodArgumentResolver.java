package core.mvc.tobe;

import core.exception.ExceptionWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.stream.Stream;

public class CommandHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameters methodParameters, final HttpRequestParameters requestParameters) {
        return methodParameters.stream()
                .anyMatch(entry -> {
                    final Class<?> commandParameterType = entry.getValue().getType();
                    return !commandParameterType.isPrimitive() &&
                            !commandParameterType.equals(String.class) &&
                            !methodParameters.isParameterTypePresent(HttpServletRequest.class);
                });
    }

    @Override
    public Object[] resolveArgument(final MethodParameters methodParameters,
                                    final HttpRequestParameters requestParameters,
                                    final HttpServletRequest request,
                                    final HttpServletResponse response) {

        return methodParameters.stream()
                .flatMap(entry -> {
                    final Class<?> commandParameter = entry.getValue().getType();
                    return newInstanceFromConstructor(commandParameter, requestParameters);
                })
                .toArray();
    }

    private Stream<?> newInstanceFromConstructor(final Class<?> commandParameter, final HttpRequestParameters requestParameters) {
        return Arrays.stream(commandParameter.getDeclaredConstructors())
                .map(ExceptionWrapper.function(constructor -> {
                    final MethodParameters constructorParameter = new MethodParameters(constructor);
                    final Object[] parameters = getParameters(constructorParameter, requestParameters);
                    return constructor.newInstance(parameters);
                }));
    }

    private Object[] getParameters(final MethodParameters methodParameters, final HttpRequestParameters requestParameters) {
        return methodParameters.stream()
                .filter(entry -> requestParameters.containsKey(entry.getKey()))
                .map(entry -> {
                    final String parameter = requestParameters.getFirst(entry.getKey());
                    return entry.getValue()
                            .convertStringTo(parameter);
                })
                .toArray();
    }

}
