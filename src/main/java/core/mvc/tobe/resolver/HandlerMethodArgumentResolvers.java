package core.mvc.tobe.resolver;

import java.util.ArrayList;
import java.util.List;

public class HandlerMethodArgumentResolvers {

    private List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = new ArrayList<>();

    public HandlerMethodArgumentResolvers() {
        BasicArgumentResolver basicArgumentResolver = new BasicArgumentResolver();
        handlerMethodArgumentResolvers.add(basicArgumentResolver);
        handlerMethodArgumentResolvers.add(new ModelAttributeMethodProcessor(basicArgumentResolver));
        handlerMethodArgumentResolvers.add(new PathArgumentResolver());
    }

    public List<HandlerMethodArgumentResolver> getHandlerMethodArgumentResolvers() {
        return handlerMethodArgumentResolvers;
    }
}
