package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {
    private List<HandlerMapping> handlerMappings;

    public HandlerMappings(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public Object getHandler(HttpServletRequest httpServletRequest) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(httpServletRequest);
            if (Objects.nonNull(handler)) {
                return handler;
            }
        }

        throw new NoExistsHandlerException("요청을 처리할 핸들러가 존재하지 않습니다.");
    }
}
