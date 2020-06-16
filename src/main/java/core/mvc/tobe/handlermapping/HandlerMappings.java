package core.mvc.tobe.handlermapping;

import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handler.exception.NotFoundHandlerException;
import core.mvc.tobe.handlermapping.exception.NotFoundHandlerMappingException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class HandlerMappings {
    private static List<HandlerMapping> handlerMappings = new ArrayList<>();

    public static void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public static void initialize(){
        handlerMappings.forEach(HandlerMapping::init);
    }

    public static List<HandlerMapping> getHandlerMappings() {
        return Collections.unmodifiableList(handlerMappings);
    }

    public static HandlerExecution findHandler(HttpServletRequest request) {
        HandlerMapping handlerMapping = findHandlerMapping(request);
        HandlerExecution handler = handlerMapping.findHandler(request);

        if(handler.isNullOrEmpty()){
            throw new NotFoundHandlerException();
        }

        return handler;
    }

    private static HandlerMapping findHandlerMapping(HttpServletRequest request){
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.hasHandler(request))
                .findFirst()
                .orElseThrow(NotFoundHandlerMappingException::new);
    }
}
