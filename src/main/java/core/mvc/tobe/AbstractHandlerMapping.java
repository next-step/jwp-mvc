package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public abstract class AbstractHandlerMapping implements HandlerMapping {
    protected static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    protected final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
