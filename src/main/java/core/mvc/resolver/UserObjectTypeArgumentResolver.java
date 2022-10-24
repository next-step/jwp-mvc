package core.mvc.resolver;

import core.mvc.tobe.MethodParameter;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.*;
import java.util.Arrays;

/**
 *  @ModelAttribute 기능 처리 가능 (어노테이션을 체크하진않는다)
  */
public class UserObjectTypeArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();

        if(isRequestBodyAnnotation(methodParameter))
            return false;

        return (isExistConstructor(parameterType) && isAllSimpleTypeInConstructorParameters(parameterType));
    }

    private boolean isExistConstructor(Class<?> methodParameter) {
        Constructor<?>[] declaredConstructors = methodParameter.getDeclaredConstructors();
        return (declaredConstructors.length >= 1);
    }

    private boolean isRequestBodyAnnotation(MethodParameter methodParameter) {
        return Arrays.stream(methodParameter.getAnnotations())
                .anyMatch(annotation -> annotation.annotationType().equals(RequestBody.class));
    }

    /*
        Simple 타입은 Wrapper 또는 Primitive Type 인 경우를 의미한다.
     */
    private boolean isAllSimpleTypeInConstructorParameters(Class<?> methodParameter) {
        Constructor<?>[] declaredConstructors = methodParameter.getDeclaredConstructors();

        Constructor<?> declaredConstructor = declaredConstructors[0];
        Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();

        return Arrays.stream(parameterTypes)
                .allMatch(type -> isSimpleAndStringType(type));
    }

    private boolean isSimpleAndStringType(Class<?> parameterType) {
        return ClassUtils.isPrimitiveOrWrapper(parameterType) || parameterType.equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParam, HttpServletRequest request, HttpServletResponse response) {
        Parameter parameter = methodParam.getParameter();
        Class<?> type = parameter.getType();
        Field[] declaredFields = type.getDeclaredFields();

        return ResolverUtility.getObject(request, type, declaredFields);
    }
}
