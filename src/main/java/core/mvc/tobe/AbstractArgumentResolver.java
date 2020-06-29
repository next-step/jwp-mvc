package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AbstractArgumentResolver implements HandlerExecution {
    private static final Logger logger = LoggerFactory.getLogger(AbstractArgumentResolver.class);

    public void applyExecution(Method method, HandlerExecution handlerExecution) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        if (requestMapping.method().equals(RequestMethod.ALL)) {
            applyExecutionByAllMethod(requestMapping, handlerExecution);
            return;
        }
        final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
        AnnotationHandlerMapping.handlerExecutions.put(handlerKey, handlerExecution);
        logger.info("Mapping Info - method : {}, value : {}, Execution : {}", requestMapping.method(), requestMapping.value(), handlerExecution);
    }

    private void applyExecutionByAllMethod(RequestMapping requestMapping, HandlerExecution handlerExecution) {
        Arrays.stream(RequestMethod.values())
                .forEach(requestMethod -> {
                    if (!requestMethod.equals(RequestMethod.ALL)) {
                        AnnotationHandlerMapping.handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMethod), handlerExecution);
                        logger.info("Mapping Info - method : {}, value : {}, Execution : {}", requestMethod, requestMapping.value(), handlerExecution);
                    }
                });
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return null;
    }
}
