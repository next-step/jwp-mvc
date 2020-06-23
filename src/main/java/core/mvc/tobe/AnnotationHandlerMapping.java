package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private final ControllerScanner controllerScanner;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(String... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
    }

    public void initialize() {
        List<Object> controllers = this.controllerScanner.getControllers();
        for (Object controller : controllers){
            Set<Method> methods = ReflectionUtils.getAllMethods(controller.getClass(), ReflectionUtils.withAnnotation(RequestMapping.class));
            methods.stream().forEach(method -> addRequestMapping(controller, method));
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private void addRequestMapping(Object controllerBean, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        Controller controller = controllerBean.getClass().getAnnotation(Controller.class);
        String url = controller.value() + requestMapping.value();
        HandlerKey handlerKey = new HandlerKey(url, requestMapping.method());
        handlerExecutions.put(handlerKey, (request, response) -> (ModelAndView) method.invoke(controllerBean, request, response));
    }

}
