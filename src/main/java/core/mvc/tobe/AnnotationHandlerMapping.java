package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import core.mvc.tobe.scanner.ComponentScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static core.mvc.support.PathVariableUtils.parse;
import static core.mvc.support.PathVariableUtils.toPathContainer;

/**
 * @author : yusik
 * @date : 2019-08-15
 */
public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private String[] basePackages;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();
    private MultiValueMap<String, HandlerKey> urlMappings = new LinkedMultiValueMap<>();

    public AnnotationHandlerMapping(String... basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public HandlerMapping initialize() {
        Map<Class<?>, Object> beans = ComponentScanner.getControllers(basePackages);
        for (Class controller : beans.keySet()) {
            registerHandler(beans.get(controller), controller.getDeclaredMethods());
        }

        return this;
    }

    private void registerHandler(Object bean, Method[] methods) {
        for (Method method : methods) {
            RequestMapping mapping = method.getDeclaredAnnotation(RequestMapping.class);
            RequestMethod[] requestMethod = mapping.method();
            String[] urls = mapping.value();
            HandlerKey handlerKey = new HandlerKey(urls, requestMethod);

            MethodParameter[] methodParameters = getMethodParameters(method, urls);

            Arrays.stream(urls).forEach(url -> urlMappings.add(url, handlerKey));
            handlerExecutions.put(handlerKey, new HandlerExecution(bean, method, methodParameters));
            logger.info("key: {}, handler: {}", handlerKey, handlerExecutions.get(handlerKey));
            logger.info("url map: {}", urlMappings);
        }
    }

    private MethodParameter[] getMethodParameters(Method method, String[] urls) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        MethodParameter[] params = new MethodParameter[parameters.length];
        for (int i = 0; i < params.length; i++) {
            params[i] = new MethodParameter(parameters[i].getAnnotations(), parameters[i].getType(), Objects.requireNonNull(parameterNames)[i], urls);
        }
        return params;
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();

        String matchKey = this.urlMappings.keySet().stream()
                .filter(url -> parse(url).matches(toPathContainer(requestUri)))
                .findFirst().orElse(requestUri);
        List<HandlerKey> handlerKeys = this.urlMappings.get(matchKey);
        HandlerKey handlerKey = handlerKeys.stream()
                .filter(key -> key.containsMethod(method))
                .findFirst()
                .orElse(null);

        return handlerExecutions.get(handlerKey);
    }
}
