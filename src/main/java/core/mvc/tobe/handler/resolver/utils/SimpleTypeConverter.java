package core.mvc.tobe.handler.resolver.utils;

public class SimpleTypeConverter {
    public <T> T convert(String value, Class<T> requireType) {
        if (int.class.isAssignableFrom(requireType) || Integer.class.isAssignableFrom(requireType)) {
            return (T) Integer.valueOf(value);
        }

        if (long.class.isAssignableFrom(requireType) || Long.class.isAssignableFrom(requireType)) {
            return (T) Long.valueOf(value);
        }

        if (short.class.isAssignableFrom(requireType) || Short.class.isAssignableFrom(requireType)) {
            return (T) Short.valueOf(value);
        }

        if (byte.class.isAssignableFrom(requireType) || Byte.class.isAssignableFrom(requireType)) {
            return (T) Byte.valueOf(value);
        }

        if (boolean.class.isAssignableFrom(requireType) || Boolean.class.isAssignableFrom(requireType)) {
            return (T) Boolean.valueOf(value);
        }

        if (float.class.isAssignableFrom(requireType) || Float.class.isAssignableFrom(requireType)) {
            return (T) Float.valueOf(value);
        }

        if (double.class.isAssignableFrom(requireType) || Double.class.isAssignableFrom(requireType)) {
            return (T) Double.valueOf(value);
        }

        return (T) value;
    }
}
