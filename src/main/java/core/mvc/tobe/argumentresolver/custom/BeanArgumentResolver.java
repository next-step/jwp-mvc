package core.mvc.tobe.argumentresolver.custom;

import core.mvc.tobe.argumentresolver.MethodArgumentResolver;
import core.mvc.tobe.argumentresolver.MethodParameter;
import core.mvc.tobe.argumentresolver.util.BeanManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class BeanArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean support(MethodParameter methodParameter) {
        return isJavaBeanType(methodParameter);
    }

    @Override
    public Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object targetBean = BeanManager.createBeanWithNoArgs(methodParameter);
        Field[] declaredFields = methodParameter.getType().getDeclaredFields();

        return BeanManager.setFields(declaredFields, request, targetBean);
    }

    private boolean isJavaBeanType(MethodParameter methodParameter) {
        return !isPrimitiveType(methodParameter) && !isStringType(methodParameter);
    }

    private boolean isStringType(MethodParameter methodParameter) {
        return methodParameter.getType().equals(String.class);
    }

    private boolean isPrimitiveType(MethodParameter methodParameter) {
        return methodParameter.getType().isPrimitive();
    }
}
