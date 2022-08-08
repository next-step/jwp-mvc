package core.mvc.tobe.handler;

import javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public Object findHandler(HttpServletRequest req) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            return handlerMapping.getHandler(req);
        }

        throw new NotExistHandlerException("요청에 맞는 핸들러가 없습니다.");
    }

    public void add(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public List<HandlerMapping> getHandlerMappings() {
        return handlerMappings;
    }
}
