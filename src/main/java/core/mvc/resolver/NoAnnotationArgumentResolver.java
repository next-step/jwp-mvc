package core.mvc.resolver;

import com.google.common.primitives.Primitives;
import core.annotation.Component;
import core.mvc.MethodParameter;
import core.mvc.exception.ArgumentResolverException;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class NoAnnotationArgumentResolver implements MethodArgumentResolver {
    private static final Set<Object> WHITE_LIST;
    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    public static final String NOT_CREATED_PARAMETER_OBJECT = "파라미터 인수 객체를 생성할 수 없습니다.";

    static {
        WHITE_LIST = Stream.of(Primitives.allPrimitiveTypes(), Primitives.allWrapperTypes())
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        WHITE_LIST.add(String.class);
    }
    @Override
    public boolean support(MethodParameter parameter) {
        return !parameter.hasParameterAnnotations();
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, String parameterName, MethodParameter parameter) throws ArgumentResolverException {
        String foundParameter = request.getParameter(parameterName);
        Class<?> pType = parameter.getParameterType();

        if (WHITE_LIST.contains(pType)) {
            return cast(pType, foundParameter);
        }

        try {
            return castObject(pType, request);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ArgumentResolverException(e);
        }
    }

    private Object castObject(Class<?> clazz, HttpServletRequest request) throws InvocationTargetException,
            InstantiationException, IllegalAccessException, ArgumentResolverException {

        Constructor<?> constructor = getSupportedConstructor(clazz, request);

        String[] parameterNames = nameDiscoverer.getParameterNames(constructor);
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        if (isValidParameterInfos(parameterNames, parameterTypes)) {
            throw new ArgumentResolverException(NOT_CREATED_PARAMETER_OBJECT);
        }

        Object[] values = extractValues(request, parameterNames, parameterTypes);

        return constructor.newInstance(values);
    }

    private static Constructor<?> getSupportedConstructor(Class<?> clazz, HttpServletRequest request) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(c -> c.getParameterCount() == request.getParameterMap().size())
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private static boolean isValidParameterInfos(String[] parameterNames, Class<?>[] parameterTypes) {
        return parameterNames != null && parameterTypes.length != parameterNames.length;
    }

    private Object[] extractValues(HttpServletRequest request, String[] parameterNames, Class<?>[] parameterTypes) {
        return IntStream.range(0, parameterTypes.length)
                .mapToObj(idx -> cast(parameterTypes[idx], request.getParameter(parameterNames[idx])))
                .toArray();
    }
}
