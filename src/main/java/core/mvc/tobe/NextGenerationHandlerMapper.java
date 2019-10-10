package core.mvc.tobe;

import core.mvc.HandlerMapper;
import core.mvc.HandlerNotFoundException;
import core.mvc.HandlingException;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Function;

public class NextGenerationHandlerMapper implements HandlerMapper {

    private static final Logger logger = LoggerFactory.getLogger(NextGenerationHandlerMapper.class);

    private final RequestParameterResolver requestParameterResolver;
    private final AnnotationHandlerMapping hm;

    public NextGenerationHandlerMapper(final RequestParameterResolver requestParameterResolver,
                                       final Object... basePackage) {
        this.requestParameterResolver = requestParameterResolver;

        hm = new AnnotationHandlerMapping(basePackage);
        hm.initialize();
    }

    @Override
    public boolean isSupport(final HttpServletRequest request) {
        return hm.getHandler(request)
                .isPresent();
    }

    @Override
    public ModelAndView mapping(final HttpServletRequest request,
                                final HttpServletResponse response) {
        return hm.getHandler(request)
                .map(handle(request, response))
                .orElseThrow(HandlerNotFoundException::new);
    }

    private Function<HandlerExecution, ModelAndView> handle(final HttpServletRequest request,
                                                            final HttpServletResponse response) {
        return handler -> {
            try {
                final Object[] parameters = requestParameterResolver.resolve(handler.getMethod(), request, response);
                return handler.handle(parameters);
            } catch (final Throwable e) {
                logger.error("Exception ", e);
                throw new HandlingException(e);
            }
        };
    }
}


