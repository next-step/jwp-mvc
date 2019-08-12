package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initMapping() {
        findControllerAnnotation();
    }

    @Override
    public core.mvc.asis.Controller findController(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private void findControllerAnnotation(){
        logger.debug("Find Controller Annotaion......");
        Reflections reflections = new Reflections(basePackage, new SubTypesScanner(), new TypeAnnotationsScanner());
        Set<Class<?>> controllerClass = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClass.stream().forEach(clazz -> {
            logger.debug("Find Controller Annotation Name {} ", clazz.getName());
            initRequestMapping(clazz);
        });
    }

    private void initRequestMapping(Class<?> clazz){
        Reflections methodFilter = new Reflections(clazz.getClassLoader(), new MethodAnnotationsScanner());
        Set<Method> mappingMethods = methodFilter.getMethodsAnnotatedWith(RequestMapping.class);
        mappingMethods.stream().forEach(method -> {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            logger.debug("Method Name {} Value {} Method {}", method.getName(), mapping.value(), mapping.method());

            HandlerKey handlerKey = new HandlerKey(mapping.value(), mapping.method());
            handlerExecutions.put(handlerKey, new HandlerExecution(method.getDeclaringClass(), method));
        });
    }
}
