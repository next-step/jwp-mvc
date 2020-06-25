package core.mvc.tobe.helper;

import core.mvc.tobe.ParameterInfo;
import core.mvc.tobe.PrimitiveTypeUtil;
import javassist.tools.web.BadHttpRequest;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.web.client.HttpClientErrorException;

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
    public boolean support(ParameterInfo parameterInfo) {
        return !parameterInfo.isOriginalType() && !parameterInfo.isAnnotated();
    }

    @Override
    public Object bindingProcess(ParameterInfo parameterInfo, HttpServletRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
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

        Constructor constructor = parameterInfo.getType().getConstructor(classes);
        return constructor.newInstance(values.toArray());
    }
}
