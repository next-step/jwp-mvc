package core.mvc.tobe;

public class ParameterTypeConverter {

    private ParameterTypeConverter() {
    }

    public static Object convert(Class<?> parameterType, String value) {
        if (parameterType.equals(int.class) || parameterType.equals(Integer.class)) {
            return Integer.parseInt(value);
        }
        if (parameterType.equals(long.class) || parameterType.equals(Long.class)) {
            return Long.parseLong(value);
        }

        return value;
    }
}
