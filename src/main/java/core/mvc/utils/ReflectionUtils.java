package core.mvc.utils;

public class ReflectionUtils {
    public static <T> T newInstance(final Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(clazz.getSimpleName() + " failed to create instance");
    }
}
