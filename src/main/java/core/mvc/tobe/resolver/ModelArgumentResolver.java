package core.mvc.tobe.resolver;

import com.google.common.primitives.Primitives;
import core.mvc.tobe.MethodParameter;
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

public class ModelArgumentResolver implements ArgumentResolver{

    private static final Set<Object> WHITE_LIST;
    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    static {
        WHITE_LIST = Stream.of(Primitives.allPrimitiveTypes(), Primitives.allWrapperTypes())
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        WHITE_LIST.add(String.class);
    }
    @Override
    public boolean support(MethodParameter methodParameter) {
        return !methodParameter.hasParameterAnnotation();
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  String parameterName) {

        String parameter = request.getParameter(parameterName);
        Class<?> parameterType = methodParameter.getParameterType();

        if (WHITE_LIST.contains(parameterType)) {
            return cast(parameterType, parameter);
        }

        Constructor<?> constructor = Arrays.stream(parameterType.getDeclaredConstructors())
                .filter(c -> c.getParameterCount() == request.getParameterMap().size())
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        String[] parameterNames = nameDiscoverer.getParameterNames(constructor);
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        Object[] values = IntStream.range(0, parameterTypes.length)
                .mapToObj(idx -> cast(parameterTypes[idx], request.getParameter(parameterNames[idx])))
                .toArray();

        try {
            return constructor.newInstance(values);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
