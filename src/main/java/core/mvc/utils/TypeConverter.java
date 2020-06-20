package core.mvc.utils;

/**
 * Converts string to primitive type
 *
 * @author hyeyoom
 */
public final class TypeConverter {

    public static Object convert(String raw, Class<?> type) {
        if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
            return Boolean.parseBoolean(raw);
        }

        if (byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)) {
            return Byte.parseByte(raw);
        }

        if (char.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type)) {
            return raw.charAt(0);
        }

        if (double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
            return Double.parseDouble(raw);
        }

        if (float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
            return Float.parseFloat(raw);
        }

        if (int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) {
            return Integer.parseInt(raw);
        }

        if (long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
            return Long.parseLong(raw);
        }

        if (short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
            return Short.parseShort(raw);
        }

        if (String.class.isAssignableFrom(type)) {
            return raw;
        }

        throw new IllegalArgumentException("Unsupported type detected: " + type);
    }
}
