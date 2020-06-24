package core.mvc.tobe;

import org.springframework.web.util.pattern.PathPattern;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By kjs4395 on 2020-06-24
 */
public class ObjectParameterHelper implements HandlerMethodHelper {

    @Override
    public Object bindingProcess(Class<?> type, String name, PathPattern pathPattern, HttpServletRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Field[] fields = type.getDeclaredFields();
        List<Class<?>> argumentTypes = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for(Field field : fields) {
            String parameterValue = request.getParameter(field.getName());

            if(parameterValue == null) {
                continue;
            }
            argumentTypes.add(field.getType());
            values.add(PrimitiveTypeUtil.castPrimitiveType(field.getType(), parameterValue));
        }

        Class[] classes = new Class[argumentTypes.size()];
        classes = argumentTypes.toArray(classes);

        Constructor constructor = type.getConstructor(classes);
        return constructor.newInstance(values.toArray());
    }
}
