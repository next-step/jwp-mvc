package core.mvc.tobe.handlermapping;

import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handler.exception.NotFoundHandlerException;
import core.mvc.tobe.handlermapping.exception.NotFoundHandlerMappingException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class HandlerMappings {
    private static Set<HandlerMapping> handlerMappings = new HashSet<>();

    public static void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public static void initialize(){
        handlerMappings.forEach(HandlerMapping::init);
    }

    public static Set<HandlerMapping> getHandlerMappings() {
        return Collections.unmodifiableSet(handlerMappings);
    }

    public static HandlerExecution findHandler(HttpServletRequest request) {
        HandlerMapping handlerMapping = findHandlerMapping(request);
        HandlerExecution handler = handlerMapping.findHandler(request);

        if(handler.isNullOrEmpty()){
            throw new NotFoundHandlerException();
        }

        return handler;
    }

    public static void clear(){
        handlerMappings.clear();
    }

    private static HandlerMapping findHandlerMapping(HttpServletRequest request){
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.hasHandler(request))
                .findFirst()
                .orElseThrow(NotFoundHandlerMappingException::new);
    }
}
