package core.mvc.tobe;

/**
 * Created By kjs4395 on 2020-06-24
 */
public class PrimitiveTypeUtil {
    public static Object castPrimitiveType(Class<?> clazz, String value) {
        if (clazz.equals(int.class)) {
            return Integer.parseInt(value);
        }

        if (clazz.equals(byte.class)) {
            return Byte.parseByte(value);
        }

        if (clazz.equals(short.class)) {
            return Short.parseShort(value);
        }

        if (clazz.equals(long.class)) {
            return Long.parseLong(value);
        }

        if (clazz.equals(float.class)) {
            return Float.parseFloat(value);
        }

        if (clazz.equals(double.class)) {
            return Double.parseDouble(value);
        }

        if (clazz.equals(boolean.class)) {
            return Boolean.parseBoolean(value);
        }

        return value;
    }
}
