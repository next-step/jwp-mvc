package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

public class HandlerAdapters {
    private Set<HandlerAdapter> handlerAdapters;

    public HandlerAdapters(Set<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final HandlerAdapter handlerAdapter = handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("요청에 대한 핸들러를 실행할 수 없습니다."));

        return handlerAdapter.handle(handler, request, response);
    }
}
