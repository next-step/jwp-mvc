package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ClassUtils;
import org.springframework.web.util.pattern.PathPattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.annotation.Annotation;
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
        PathPattern pathPattern = PathPatternUtil.getPathPattern(method.getDeclaredAnnotation(RequestMapping.class).value());
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for(int i=0; i<parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];

            if(parameterAnnotations[i].length >0) {
                //annotaion이 존재한다는 소리
                AnnotationParameterHelper annotationParameterHelper = new AnnotationParameterHelper();
                bindObjects[i] =annotationParameterHelper.bindingProcess(parameterType, names[i], pathPattern, request);
                continue;
            }

            if(ClassUtils.isPrimitiveOrWrapper(parameterType)) {
                OriginalParameterBinding originalParameterBinding = new OriginalParameterBinding();
                bindObjects[i] = originalParameterBinding.bindingProcess(parameterType, names[i],pathPattern, request);
                continue;
            }  //object 라고 간주

            ObjectParameterHelper objectParameterHelper = new ObjectParameterHelper();
            bindObjects[i] = objectParameterHelper.bindingProcess(parameterType, names[i],pathPattern, request);

        }
        return (ModelAndView) method.invoke(clazz.newInstance(),bindObjects);
    }

}
