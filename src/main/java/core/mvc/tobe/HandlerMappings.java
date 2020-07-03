package core.mvc.tobe;

import core.mvc.asis.RequestMapping;
import core.mvc.scanner.WebApplicationScanner;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HandlerMappings {

    private final List<HandlerMapping> mappings = new ArrayList<>();

    public void init(WebApplicationScanner webApplicationScanner) {
        mappings.addAll(Arrays.asList(
                new RequestMapping(),
                new AnnotationHandlerMapping(webApplicationScanner)
        ));

        for (HandlerMapping mapping : mappings) {
            mapping.initialize();
        }
    }

    public HandlerMethod getHandlerMethod(HttpServletRequest request) {
        return mappings.stream()
                .filter(handlerMapping -> handlerMapping.supports(request))
                .map(handlerMapping -> handlerMapping.getHandlerMethod(request))
                .findFirst()
                .orElse(null);
    }
}
