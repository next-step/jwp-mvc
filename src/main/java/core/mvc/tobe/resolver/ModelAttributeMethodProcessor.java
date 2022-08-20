package core.mvc.tobe.resolver;

import core.mvc.tobe.resolver.method.MethodParameter;
import next.model.User;
import org.checkerframework.checker.signature.qual.PrimitiveType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class ModelArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return !parameter.isPrimitiveType();
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Class<?> parameterType = methodParameter.getParameterType();
        Constructor<?>[] declaredConstructors = parameterType.getDeclaredConstructors();
        Constructor<?> declaredConstructor = declaredConstructors[0];

        Parameter[] parameters = declaredConstructor.getParameters();
        for (Parameter parameter : parameters) {

        }
        declaredConstructor.setAccessible(true);

//        Arrays.stream(declaredConstructor.getParameterTypes())
//                .map(clazz -> clazz.fi)
        return null;
    }
}
