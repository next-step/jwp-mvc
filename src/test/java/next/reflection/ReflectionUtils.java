package next.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils<T> {

    public void setPrivateField(T t, Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(t, value);
    }

    public static void executeMethod(Constructor<?> defaultConstructor, Method it) {
        try {
            it.invoke(defaultConstructor.newInstance());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
