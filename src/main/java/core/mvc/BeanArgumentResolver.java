package core.mvc;

import core.mvc.support.PathVariableUtils;
import core.mvc.tobe.MethodParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class BeanArgumentResolver implements ArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(BeanArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Object.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        Class<?> type = parameter.getParameterType();
        Object parameterInstance = newInstance(type);

        for (Field field : type.getDeclaredFields()) {
            String param = request.getParameter(field.getName());
            if (Objects.nonNull(param)) {
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, parameterInstance, PathVariableUtils.toPrimitiveValue(field.getType(), param));
            }
        }

        return parameterInstance;
    }

    private Object newInstance(Class<?> type) {
        try {
            Constructor constructor = type.getDeclaredConstructor();
            ReflectionUtils.makeAccessible(constructor);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            logger.error("create instance error : {}", type.getTypeName(), e);
        }
        return null;
    }
}