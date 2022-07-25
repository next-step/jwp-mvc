package core.mvc.supporter;

public class TypeConverter {

    public static Object convert(Class<?> parameterType, String value) {
        if (parameterType.equals(int.class)) {
            return Integer.parseInt(value);
        }
        if (parameterType.equals(long.class)) {
            return Long.parseLong(value);
        }
        if (parameterType.equals(double.class)) {
            return Double.parseDouble(value);
        }
        if (parameterType.equals(float.class)) {
            return Float.parseFloat(value);
        }
        if (parameterType.equals(short.class)) {
            return Short.parseShort(value);
        }
        if (parameterType.equals(boolean.class)) {
            return Boolean.parseBoolean(value);
        }
        if (parameterType.equals(byte.class)) {
            return Byte.parseByte(value);
        }
        if (parameterType.equals(char.class)) {
            return value.charAt(0);
        }

        return value;
    }
}
