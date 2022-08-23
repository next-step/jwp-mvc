package core.mvc.tobe.resolver;

import org.springframework.lang.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandlerMethodArgumentResolvers {

    private List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = new ArrayList<>();

    public HandlerMethodArgumentResolvers(List<HandlerMethodArgumentResolver>  handlerMethodArgumentResolvers) {
       this.handlerMethodArgumentResolvers = handlerMethodArgumentResolvers;
    }

    public static HandlerMethodArgumentResolvers of(List<HandlerMethodArgumentResolver> argumentResolvers) {
        return new HandlerMethodArgumentResolvers(argumentResolvers);
    }

    public HandlerMethodArgumentResolvers addResolvers(
            @Nullable HandlerMethodArgumentResolver... resolvers) {

        if (resolvers != null) {
            Collections.addAll(this.handlerMethodArgumentResolvers, resolvers);
        }
        return this;
    }

    public List<HandlerMethodArgumentResolver> getHandlerMethodArgumentResolvers() {
        return handlerMethodArgumentResolvers;
    }
}
