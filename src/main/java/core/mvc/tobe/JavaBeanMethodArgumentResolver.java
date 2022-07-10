package core.mvc.tobe;

import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

public class JavaBeanMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(Parameter parameter) {
        if (BeanUtils.isSimpleProperty(parameter.getType()) || BeanUtils.isSimpleValueType(parameter.getType())) {
            return false;
        }
        return true;
    }

    @Override
    public Object resolveArgument(Parameter parameter, String parameterName, HttpServletRequest httpServletRequest) {
        Class<?> type = parameter.getType();
        Object newInstance = null;
        try {
            newInstance = type.getDeclaredConstructor().newInstance();
            Field[] fields = newInstance.getClass().getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                field.set(newInstance, ParameterTypeConverter.convert(field.getType(), httpServletRequest.getParameter(field.getName())));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return newInstance;
    }
}
