package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        initialize();
    }

    public void initialize() {
        for (final Object o : basePackage) {
            Reflections reflections = new Reflections(
                    new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage((String) o)));

            AnnotationHandler controllerAnnotationHandler = new ControllerAnnotationHandler(reflections);
            controllerAnnotationHandler.init();
            handlerExecutions.putAll(controllerAnnotationHandler.getExecutionMap());
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
