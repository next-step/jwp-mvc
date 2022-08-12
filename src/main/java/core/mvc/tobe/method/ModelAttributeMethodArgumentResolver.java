package core.mvc.tobe.method;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

class ModelAttributeMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final ParameterNameDiscoverer NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    private final SimpleTypeConverter converter;

    private ModelAttributeMethodArgumentResolver(SimpleTypeConverter converter) {
        Assert.notNull(converter, "'converter' must not be null");
        this.converter = converter;
    }

    static ModelAttributeMethodArgumentResolver from(SimpleTypeConverter converter) {
        return new ModelAttributeMethodArgumentResolver(converter);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return !converter.isSupports(parameter.type());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return instanceParameter(resolvableConstructor(parameter.type()), request);
    }

    private Object instanceParameter(Constructor<?> constructor, HttpServletRequest request) {
        try {
            return instance(constructor, request);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private Object instance(Constructor<?> constructor, HttpServletRequest request) throws ReflectiveOperationException {
        String[] parameterNames = NAME_DISCOVERER.getParameterNames(constructor);
        if (constructor.getParameterCount() == 0 || parameterNames == null) {
            return constructor.newInstance();
        }
        return constructor.newInstance(extractedArgs(constructor, request, parameterNames));
    }

    private Object[] extractedArgs(Constructor<?> constructor, HttpServletRequest request, String[] parameterNames) {
        Iterator<String> namesIterator = List.of(parameterNames).iterator();
        return Stream.of(constructor.getParameterTypes())
                .map(type -> convertedParameter(type, namesIterator.next(), request))
                .toArray(Object[]::new);
    }

    private Object convertedParameter(Class<?> type, String name, HttpServletRequest request) {
        if (converter.isSupports(type)) {
            return converter.convert(request.getParameter(name), type);
        }
        return instanceParameter(resolvableConstructor(type), request);
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<T> resolvableConstructor(Class<T> clazz) {
        return (Constructor<T>) Stream.of(clazz.getConstructors())
                .findAny()
                .orElseThrow(() -> new IllegalStateException(
                        String.format("no primary or single unique constructor found for %s", clazz)));
    }
}
