package core.mvc.resolver;

public class PrimitiveConverter {

    public static Object convert(Class<?> parameterType, String object) {

        if(parameterType.equals(int.class))
            return Integer.parseInt(object);
        else if(parameterType.equals(double.class))
            return Double.parseDouble(object);
        else if(parameterType.equals(long.class))
            return Long.parseLong(object);
        else if(parameterType.equals(float.class))
            return Float.parseFloat(object);

        return object;
    }
}
