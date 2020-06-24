package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class HandlerExecution {
    private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);

    private final Class<?> clazz;
    private final Method method;

    public HandlerExecution(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {


        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] bindObjects = new Object[parameterTypes.length];
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] names = nameDiscoverer.getParameterNames(method);

        for(int i=0; i<parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];

            if(ClassUtils.isPrimitiveOrWrapper(parameterType)) {
                OriginalParameterBinding originalParameterBinding = new OriginalParameterBinding();
                bindObjects[i] = originalParameterBinding.bindingProcess(parameterType, names[i], request);
                continue;
            }  //object 라고 간주

            ObjectParameterHelper objectParameterHelper = new ObjectParameterHelper();
            bindObjects[i] = objectParameterHelper.bindingProcess(parameterType, names[i], request);

        }
        return (ModelAndView) method.invoke(clazz.newInstance(),bindObjects);
    }

}
