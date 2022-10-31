package core.mvc.resolver;

import core.mvc.tobe.MethodParameter;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * @RequestBody 어노테이션은 생략이 불가능하다. 그래서 UserObjectTypeArgumentResolver로 대체할 수 없음.
 * 생략하면 @ModelAttribute(UserObjectTypeArgumentResolver) 처리됨.
 */
public class RequestBodyArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();

        if(!isRequestBodyAnnotation(parameter))
            return false;

        return (isExistConstructor(parameterType) && isAllSimpleTypeInConstructorParameters(parameterType));
    }

    private boolean isRequestBodyAnnotation(MethodParameter parameter) {
        return Arrays.stream(parameter.getAnnotations()).
                anyMatch(annotation -> annotation.annotationType().equals(RequestBody.class));
    }


    private boolean isExistConstructor(Class<?> methodParameter) {
        Constructor<?>[] declaredConstructors = methodParameter.getDeclaredConstructors();
        return (declaredConstructors.length >= 1);
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
    public Object resolveArgument(MethodParameter methodParam, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Parameter parameter = methodParam.getParameter();
        Class<?> type = parameter.getType();
        Field[] declaredFields = type.getDeclaredFields();

        return ResolverUtil.getObject(request, type, declaredFields);
    }
}
