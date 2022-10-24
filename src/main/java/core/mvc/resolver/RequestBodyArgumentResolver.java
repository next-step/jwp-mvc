package core.mvc.resolver;

import core.mvc.tobe.MethodParameter;
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
        return Arrays.stream(parameter.getAnnotations()).
                anyMatch(annotation -> annotation.annotationType().equals(RequestBody.class));
    }

    @Override
    public Object resolveArgument(MethodParameter methodParam, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Parameter parameter = methodParam.getParameter();
        Class<?> type = parameter.getType();
        Field[] declaredFields = type.getDeclaredFields();

        return getObject(request, type, declaredFields);
    }

    private Object getObject(HttpServletRequest request, Class<?> type, Field[] declaredFields) {
        Object[] objects = new Object[declaredFields.length];
        int i = 0;
        for (Field field : declaredFields) {
            objects[i++] = request.getParameter(field.getName());
        }

        return getObjectByConstructor(type, objects);
    }

    private Object getObjectByConstructor(Class<?> type, Object[] objects) {
        Object o = null;

        try {
            Constructor<?>[] declaredConstructors = type.getDeclaredConstructors();
            Constructor<?> declaredConstructor = declaredConstructors[0];
            o = declaredConstructor.newInstance(objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
}
