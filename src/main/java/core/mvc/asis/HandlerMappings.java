package core.mvc.asis;

import com.google.common.collect.Lists;
import core.mvc.HandlerMapping;


import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class HandlerMappings {

    private final List<HandlerMapping> mappings = Lists.newArrayList();

    public HandlerMappings(HandlerMapping... handlerMappings) {
        this.mappings.addAll(Arrays.asList(handlerMappings));
    }

    public Object getHandler(HttpServletRequest req) {
        for (HandlerMapping mapping : mappings) {
            Object handler = mapping.getHandler(req);
            if (handler != null) return handler;
        }
        return null;
    }
}
