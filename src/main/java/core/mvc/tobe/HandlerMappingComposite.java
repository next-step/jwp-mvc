package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author KingCjy
 */
public class HandlerMappingComposite implements HandlerMapping {

    private Set<HandlerMapping> handlerMappings = new LinkedHashSet<>();

    public HandlerMappingComposite(HandlerMapping... handlerMappings) {
        this.handlerMappings.addAll(Arrays.asList(handlerMappings));
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.getHandler(request) != null)
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .findAny()
                .get();
    }
}
