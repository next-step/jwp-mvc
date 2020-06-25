package core.mvc.tobe.helper;

import core.mvc.tobe.ParameterInfo;
import core.mvc.tobe.PrimitiveTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger logger = LoggerFactory.getLogger(ObjectParameterHelper.class);

    @Override
    public boolean support(ParameterInfo parameterInfo) {
        return !parameterInfo.isOriginalType() && !parameterInfo.isAnnotated();
    }

    @Override
    public Object bindingProcess(ParameterInfo parameterInfo, HttpServletRequest request) {
        Field[] fields = parameterInfo.getType().getDeclaredFields();
        List<Class<?>> argumentTypes = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Field field : fields) {
            String parameterValue = request.getParameter(field.getName());

            if (parameterValue == null) {
                continue;
            }
            argumentTypes.add(field.getType());
            values.add(PrimitiveTypeUtil.castPrimitiveType(field.getType(), parameterValue));
        }

        Class[] classes = new Class[argumentTypes.size()];
        classes = argumentTypes.toArray(classes);

        Constructor constructor = null;
        try {
            constructor = parameterInfo.getType().getConstructor(classes);
            return constructor.newInstance(values.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Paramter Bind Exception Constructor Invoke : {}", e);
        } catch (NoSuchMethodException e) {
            logger.error("Parameter Bind Exception Not Found Constructor : {}", e);
        }
        return null;
    }
}
