package core.mvc.resolver;

import java.lang.reflect.Parameter;

public class ParameterUtils {

    static boolean supportPrimitiveType(MethodParameter methodParameter){
        Parameter parameter = methodParameter.toParameter();
        if(parameter.getParameterizedType().equals(int.class)){
            return true;
        }

        if(parameter.getParameterizedType().equals(long.class)){
            return true;
        }

        if(parameter.getParameterizedType().equals(String.class)){
            return true;
        }

        return false;
    }

    static Object convertToPrimitiveType(Parameter parameter, String variable) {
        return convertToPrimitiveType(parameter.getType(), variable);
    }

    static Object convertToPrimitiveType(Class clazz, String variable) {
        if (clazz.equals(int.class)) {
            return Integer.parseInt(variable);
        }

        if (clazz.equals(long.class)) {
            return Long.parseLong(variable);
        }

        return variable;
    }

}
