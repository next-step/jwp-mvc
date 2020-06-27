package core.mvc.resolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class BeanHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter method) {
        return !ParameterUtils.supportPrimitiveType(method);
    }

    @Override
    public Object resolve(MethodParameter methodParameter, String mappingUrl,
        HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        if (!supportsParameter(methodParameter)) {
            throw new IllegalArgumentException("unSupports Parameter");
        }

        Parameter parameter = methodParameter.toParameter();
        Object bean = createBean(parameter);

        Field[] fields = parameter.getType().getDeclaredFields();
        for (Field field : fields) {
            if (httpRequest.getParameter(field.getName()) == null) {
                continue;
            }
            try {
                field.setAccessible(true);
                field.set(bean, ParameterUtils.convertToPrimitiveType(field.getType(),
                    httpRequest.getParameter(field.getName())));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        return bean;
    }

    private Object createBean(Parameter parameter) {
        try {
            Constructor constructor = parameter.getType().getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
