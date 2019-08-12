package core.mvc.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static Constructor[] getAllConstrollers(Class<?> clazz) {
        return clazz.getDeclaredConstructors();
    }

    public static Method[] getAllMethods(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    }

    public static Method[] getAllFields(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    }

}
