package core.mvc.tobe.resolver;

import com.sun.nio.sctp.IllegalUnbindException;
import core.mvc.tobe.resolver.method.MethodParameter;
import next.model.User;
import org.checkerframework.checker.signature.qual.PrimitiveType;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public class ModelAttributeMethodProcessor implements HandlerMethodArgumentResolver {

    private final BasicArgumentResolver basicArgumentResolver;

    public ModelAttributeMethodProcessor(BasicArgumentResolver basicArgumentResolver) {
        this.basicArgumentResolver = basicArgumentResolver;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return !parameter.isPrimitiveType();
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        Class<?> parameterType = methodParameter.getParameterType();
        if (Objects.isNull(basicArgumentResolver.getObject(parameterType, request.getParameterNames().nextElement()))) {
            return basicArgumentResolver.resolveArgument(methodParameter, request, response);
        }

        Constructor<?> constructor = parameterType.getDeclaredConstructors()[0];

        String[] parameterNames = methodParameter.getParameterNames(constructor);
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        Object[] objects = getObject(request, parameterTypes, parameterNames);

        try {
            return constructor.newInstance(objects);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Throwable rootCauseException = e.getCause();
            if (Objects.nonNull(rootCauseException)) {
                throw new IllegalUnbindException(rootCauseException.getMessage());
            }
            throw new Error(e);
        }
    }

    private Object[] getObject(HttpServletRequest request, Class<?>[] parameterTypes, String[] parameterName) {
        return IntStream.range(0, parameterTypes.length)
                .mapToObj(i -> basicArgumentResolver.getObject(parameterTypes[i], request.getParameter(parameterName[i])))
                .toArray();
    }
}
