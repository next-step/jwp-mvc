package core.mvc.tobe;

import core.annotation.web.PathVariable;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ArgumentResolvers {

    public static void initialize(Class clazz, Method method) {
        if (isPathVariable(method)) {
            new PathVariableArgumentResolver(clazz, method);
            return;
        }
        classResolver(clazz, method);
    }

    private static void classResolver(Class clazz, Method method) {
        if (isDefaultClass(method)) {
            new BasicTypeArgumentResolver(clazz, method);
            return;
        }
        classResolverByInterface(clazz, method);
    }

    private static void classResolverByInterface(Class clazz, Method method) {
        if (isInterface(method)) {
            new ServletArgumentResolver(clazz, method);
            return;
        }
        new BeanTypeArgumentResolver(clazz, method);
    }

    private static boolean isPathVariable(Method method) {
        return Arrays.stream(method.getParameterAnnotations())
                .flatMap(Arrays::stream)
                .anyMatch(annotation1 -> annotation1 instanceof PathVariable);
    }

    private static boolean isDefaultClass(Method method) {
        return Arrays.stream(method.getParameterTypes())
                .anyMatch(m -> m.isInstance(String.class) || m.isPrimitive());
    }

    private static boolean isInterface(Method method) {
        return Arrays.stream(method.getParameterTypes())
                .anyMatch(Class::isInterface);
    }

}
