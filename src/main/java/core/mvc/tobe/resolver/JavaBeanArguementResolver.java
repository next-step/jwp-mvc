package core.mvc.tobe.resolver;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.NoSuchElementException;

public class JavaBeanArguementResolver implements HandlerMethodArgumentResolver {

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public boolean isSupport(Class<?> parameterType) {
        return parameterType.getConstructors().length > 0 && parameterType != String.class;
    }

    @Override
    public Object resolve(HttpServletRequest request, String parameterName, Class<?> parameterType) {
        Constructor constructor = parameterType.getConstructors()[0];

        Parameter[] parameters = constructor.getParameters();
        String[] constructorParameterNames = nameDiscoverer.getParameterNames(constructor);
        Object[] constructorValues = new Object[parameters.length];

        for (int j = 0; j < parameters.length; j++) {
            Class<?> constructorParameterType = parameters[j].getType();


            String constructorParameterName = constructorParameterNames[j];

            String value = request.getParameter(constructorParameterName);
            constructorValues[j] = value;
            if (constructorParameterType.equals(int.class)) {
                constructorValues[j] = Integer.parseInt(value);
            }
            if (constructorParameterType.equals(long.class)) {
                constructorValues[j] = Long.parseLong(value);
            }
        }

        try {
            Object obj = constructor.newInstance(constructorValues);
            return obj;
        } catch (Throwable ex) {
            throw new NoSuchElementException("constructor 가 없습니다.");
        }
    }
}
