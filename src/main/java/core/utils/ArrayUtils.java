package core.utils;

public class ArrayUtils {
    private ArrayUtils() {
        throw new IllegalStateException("You can't create new util class");
    }

    public static boolean isEmpty(Object[] array) {
        return (array.length == 0);
    }
}
