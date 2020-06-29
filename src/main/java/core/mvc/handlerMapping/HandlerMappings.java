package core.mvc.handlerMapping;

import com.google.common.collect.Lists;
import core.mvc.support.exception.HandlerNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {
    private List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        handlerMappings = Lists.newArrayList();
    }

    public void add(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest req) {
        return handlerMappings.stream()
                .map(hm -> hm.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new HandlerNotFoundException(req.getRequestURI(), req.getMethod()));
    }
}
