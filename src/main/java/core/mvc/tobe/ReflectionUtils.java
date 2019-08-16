package core.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtils {

    public static Constructor[] getAllConstrollers(Class<?> clazz) {
        return clazz.getDeclaredConstructors();
    }

    public static Method[] getAllMethods(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    }

    public static Method[] getAllMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .toArray(Method[]::new);
    }

    public static Method[] getAllFields(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    }

}
