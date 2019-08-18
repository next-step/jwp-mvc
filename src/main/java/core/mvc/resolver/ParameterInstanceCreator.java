package core.mvc.resolver;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ParameterInstanceCreator {

    public <T> T create(Class<T> type, HttpServletRequest request) {
        try {
            T instance = newInstance(type);
            setFieldValues(type.getDeclaredFields(), instance, request);
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("인스턴스 생성 중 오류가 발생하였습니다.", e);
        }
    }

    private <T> T newInstance(Class<T> type) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<T> defaultConstructor;
        try {
            defaultConstructor = type.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("기본 생성자가 없습니다. type : ["+ type.getName() +"]");
        }
        defaultConstructor.setAccessible(true);
        return defaultConstructor.newInstance();
    }

    private <T> void setFieldValues(Field[] fields, T instance, HttpServletRequest request) throws IllegalAccessException {
        for (Field field : fields) {
            setFieldValue(field, instance, request.getParameter(field.getName()));
        }
    }

    private <T> void setFieldValue(Field field, T instance, String parameterValue) throws IllegalAccessException {
        if (StringUtils.isEmpty(parameterValue)) {
            return;
        }
        field.setAccessible(true);

        Object fieldValue = TypeConverter.convert(field.getType(), parameterValue).orElse(null);
        field.set(instance, fieldValue);
    }

}
