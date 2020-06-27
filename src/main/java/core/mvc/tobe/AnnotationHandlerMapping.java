package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.resolver.HandlerMethodArgumentResolver;
import core.mvc.resolver.HandlerMethodArgumentResolverComposite;
import core.mvc.resolver.MethodParameter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private final ControllerScanner controllerScanner;
    private final HandlerMethodArgumentResolver HandlerMethodArgumentResolver;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(HandlerMethodArgumentResolver handlerMethodArgumentResolver ,String... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.HandlerMethodArgumentResolver = handlerMethodArgumentResolver;
    }

    public AnnotationHandlerMapping(String... basePackage) {
        this(HandlerMethodArgumentResolverComposite.newInstance(), basePackage);
    }

    public void initialize() {
        List<Object> controllers = this.controllerScanner.getControllers();
        for (Object controller : controllers){
            Set<Method> methods = ReflectionUtils.getAllMethods(controller.getClass(), ReflectionUtils.withAnnotation(RequestMapping.class));
            methods.stream().forEach(method -> addRequestMapping(controller, method));
        }
    }

    @Override
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
        handlerExecutions.put(handlerKey, (request, response) -> {
            Object[] args = IntStream.range(0, method.getParameterCount())
                .mapToObj(value -> HandlerMethodArgumentResolver.resolve(new MethodParameter(method, value), url, request, response))
                .toArray();

            return (ModelAndView) method.invoke(controllerBean, args);
        });
    }

}
