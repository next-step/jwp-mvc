package core.mvc.tobe.resolver.argument;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.NoSuchElementException;

public class JavaBeanArgumentResolver implements HandlerMethodArgumentResolver {

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public boolean isSupport(MethodParameter methodParameter) {
        return methodParameter.isJavaBeanArgument();
    }

    @Override
    public Object resolve(HttpServletRequest request, MethodParameter methodParameter) {
        Constructor constructor = methodParameter.getParameterType().getConstructors()[0];

        Parameter[] parameters = constructor.getParameters();
        String[] constructorParameterNames = nameDiscoverer.getParameterNames(constructor);
        Object[] constructorValues = new Object[parameters.length];

        for (int j = 0; j < parameters.length; j++) {
            Class<?> constructorParameterType = parameters[j].getType();
            String constructorParameterName = constructorParameterNames[j];
            String constructorParameterValue = request.getParameter(constructorParameterName);

            constructorValues[j] = methodParameter.getParameterTypeValue(constructorParameterValue, constructorParameterType);
        }

        return createInstance(constructor, constructorValues);
    }

    private Object createInstance(Constructor constructor, Object[] constructorValues) {
        try {
            Object obj = constructor.newInstance(constructorValues);
            return obj;
        } catch (Throwable ex) {
            throw new NoSuchElementException("constructor 가 없습니다.");
        }
    }
}
