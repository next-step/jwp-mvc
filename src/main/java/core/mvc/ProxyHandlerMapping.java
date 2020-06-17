package core.mvc;

import core.mvc.asis.RequestMapping;
import core.mvc.exceptions.HandlerNotFoundException;
import core.mvc.tobe.AnnotationHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Incrementally migrates a legacy module, RequestMapping for this case, by gradually replacing new mvc module.
 * This object intercepts the request and determines which handler should process the request.
 * <p>
 * Unlike its name, this is actually facade pattern. not proxy pattern.
 *
 * @author hyeyoom
 */
public class ProxyHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(ProxyHandlerMapping.class);

    private final List<HandlerMapping> handlerMappingList = new ArrayList<>();

    public ProxyHandlerMapping() {
        final RequestMapping rm = new RequestMapping();
        rm.initMapping();
        handlerMappingList.add(rm);
        handlerMappingList.add(new AnnotationHandlerMapping("next.controller"));

    }

    @Override
    public Object getHandler(HttpServletRequest request) throws HandlerNotFoundException {
        final String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        final Optional<Object> maybeHandler = handlerMappingList.stream()
                .map(handlerMapping -> {
                    try {
                        return handlerMapping.getHandler(request);
                    } catch (HandlerNotFoundException ignore) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst();

        if (maybeHandler.isPresent()) {
            return maybeHandler.get();
        }
        throw new HandlerNotFoundException("핸들러가 존재하지 않아요..!");
    }
}
