package core.mvc.tobe.argumentresolver.util;

public class ParameterUtil {
    public static Object parseWithType(String parameter, Class<?> clazz){
        if (isPrimitiveInt(clazz)) {
            return Integer.parseInt(parameter);
        }

        if (isPrimitiveLong(clazz)) {
            return Long.valueOf(parameter).longValue();
        }

        return parameter;
    }

    private static boolean isPrimitiveInt(Class clazz) {
        return int.class.equals(clazz);
    }

    private static boolean isPrimitiveLong(Class clazz) {
        return long.class.equals(clazz);
    }
}
