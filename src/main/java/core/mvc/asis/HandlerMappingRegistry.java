package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.web.exception.NotFoundHandlerException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMappingRegistry {

    private List<HandlerMapping> mappingList = new ArrayList<>();

    public void add(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        mappingList.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return mappingList.stream()
                .map(mapping -> mapping.getHandler(request))
                .filter(mapping -> mapping != null)
                .findFirst()
                .orElseThrow(() -> new NotFoundHandlerException("handler 를 찾을 수 없습니다"));
    }
}
