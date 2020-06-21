package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class AnnotationHandlerMapping {

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        // TODO: 2020/06/21 annotation scan
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
