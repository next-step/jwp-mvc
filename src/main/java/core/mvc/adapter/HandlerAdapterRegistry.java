package core.mvc.adapter;

import core.web.exception.NotFoundAdapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private List<HandlerAdapter> adapterList = new ArrayList<>();

    public void add(HandlerAdapter handlerAdapter) {
        adapterList.add(handlerAdapter);
    }
    public HandlerAdapter getAdapter(Object handler) {
        return adapterList.stream()
                .filter(ad -> ad.support(handler))
                .findFirst()
                .orElseThrow(() -> {
                    throw new NotFoundAdapter("adapter 를 찾을 수 없습니다");
                });
    }
}
