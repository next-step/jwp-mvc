package core.mvc.tobe;

public class BeanUtils {

    private BeanUtils() {
    }

    public static boolean isSimpleValueType(Class<?> type) {
        return (ClassUtils.isPrimitiveOrWrapper(type)
            || CharSequence.class.isAssignableFrom(type)
            || Number.class.isAssignableFrom(type));
    }
}
