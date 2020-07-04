package core.mvc.tobe.argumentresolver.custom;

import core.mvc.tobe.argumentresolver.MethodArgumentResolver;
import core.mvc.tobe.argumentresolver.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IntegerArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean support(MethodParameter methodParameter) {
        Class<?> type = methodParameter.getType();
        return type.equals(int.class) || type.equals(long.class);
    }

    @Override
    public Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        Class<?> type = methodParameter.getType();
        String parameter = request.getParameter(methodParameter.getName());

        if(isPrimitiveInt(type)){
            return Integer.parseInt(parameter);
        }

        if(isPrimitiveLong(type)){
            return Long.valueOf(parameter).longValue();
        }

        return null;
    }

    private boolean isPrimitiveInt(Class clazz){
        return int.class.equals(clazz);
    }

    private boolean isPrimitiveLong(Class clazz){
        return long.class.equals(clazz);
    }
}
