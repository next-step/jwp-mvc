package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMethod;
import org.reflections8.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements RequestHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class, true);

        controllers.stream()
                .forEach(c -> {
                    RequestMappingHandlerUtils helper = new RequestMappingHandlerUtils(c);
                    handlerExecutions.putAll(helper.makeRequestMappingHandlerMap());
                });
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
