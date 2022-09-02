package core.mvc.tobe.method.support;

import core.mvc.tobe.method.HandlerMethodArgumentResolver;
import core.mvc.tobe.method.MethodParameter;
import core.mvc.tobe.method.SimpleTypeConverter;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ModelAttributeMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private final SimpleTypeConverter typeConverter;

    public ModelAttributeMethodArgumentResolver(SimpleTypeConverter typeConverter) {
        Assert.notNull(typeConverter, "SimpleTypeConverter가 null이어선 안됩니다.");
        this.typeConverter = typeConverter;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return !typeConverter.isSupports(parameter.getType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return instantiateParam(resolvableConstructor(parameter.getType()), request);
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<T> resolvableConstructor(Class<?> clazz) {
        return (Constructor<T>) Stream.of(clazz.getConstructors())
                .findAny()
                .orElseThrow(() -> new IllegalStateException(String.format("%s의 생성자를 찾지 못했습니다.", clazz)));
    }

    private Object instantiateParam(Constructor<?> constructor, HttpServletRequest request) {
        try {
            return instantiate(constructor, request);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private Object instantiate(Constructor<?> constructor, HttpServletRequest request) throws
            InvocationTargetException, InstantiationException, IllegalAccessException {

        String[] parameterNames = nameDiscoverer.getParameterNames(constructor);
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
        if (typeConverter.isSupports(type)) {
            return typeConverter.convert(request.getParameter(name), type);
        }
        return instantiateParam(resolvableConstructor(type), request);
    }
}
