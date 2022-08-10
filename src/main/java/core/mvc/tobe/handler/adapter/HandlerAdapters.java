package core.mvc.tobe.handler.adapter;

import core.mvc.tobe.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HandlerAdapters {
    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public ModelAndView handle(Object target, HttpServletRequest request, HttpServletResponse response) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.support(target))
                .map(adapter -> adapter.handle(request, response, target))
                .findFirst()
                .orElseThrow(() -> new NoExistsAdapterException("요청을 처리하기 위해 핸들러를 실행할 어댑터가 존재하지 않습니다."));
    }
}
