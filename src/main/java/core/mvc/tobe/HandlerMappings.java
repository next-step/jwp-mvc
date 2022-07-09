package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {

    private List<HandlerMapping> handlerMappingList;

    public HandlerMappings(List<HandlerMapping> handlerMappingList) {
        this.handlerMappingList = handlerMappingList;
    }

    public void initialize() {
        handlerMappingList.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest req) {
        return handlerMappingList.stream()
                .filter(handlerMapping -> Objects.nonNull(handlerMapping.getHandler(req)))
                .findFirst();
    }
}
