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
        new BasicTypeArgumentResolver(clazz, method);
    }

    private static boolean isPathVariable(Method method) {
        return Arrays.stream(method.getParameterAnnotations())
                .flatMap(Arrays::stream)
                .anyMatch(annotation1 -> annotation1 instanceof PathVariable);
    }

}
