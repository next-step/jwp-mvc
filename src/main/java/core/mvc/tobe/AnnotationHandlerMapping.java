package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

public class AnnotationHandlerMapping {

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage,
            new MethodAnnotationsScanner(),
            new TypeAnnotationsScanner(),
            new SubTypesScanner()
        );

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Method> requestMappings = reflections.getMethodsAnnotatedWith(RequestMapping.class);

        for(Method method : requestMappings){
            if (!controllers.contains(method.getDeclaringClass())) {
                continue;
            }
            Object controllerObj = null;
            try {
                controllerObj = method.getDeclaringClass().newInstance();
            } catch (Exception e) {
                throw new RuntimeException();
            }

            Controller controller = method.getDeclaringClass().getAnnotation(Controller.class);
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            String url = controller.path() + requestMapping.value();
            HandlerKey handlerKey = new HandlerKey(url, requestMapping.method());

            Object finalControllerObj = controllerObj;
            handlerExecutions.put(handlerKey, new HandlerExecution() {
                @Override
                public ModelAndView handle(HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
                    return (ModelAndView) method.invoke(finalControllerObj, request, response);
                }
            });
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
