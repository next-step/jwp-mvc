package core.mvc.tobe;

import core.mvc.HandlerMapper;
import core.mvc.HandlerNotFoundException;
import core.mvc.HandlingException;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class NextGenerationHandlerMapper implements HandlerMapper {

    private static final Logger logger = LoggerFactory.getLogger(NextGenerationHandlerMapper.class);

    private static final String DEFAULT_BASE_PACKAGE = "next.controller";

    private final AnnotationHandlerMapping hm;

    public NextGenerationHandlerMapper() {
        hm = new AnnotationHandlerMapping(DEFAULT_BASE_PACKAGE);
        hm.initialize();
    }

    @Override
    public boolean isSupport(final HttpServletRequest request) {
        return Objects.nonNull(hm.getHandler(request));
    }

    @Override
    public ModelAndView mapping(final HttpServletRequest request,
                                final HttpServletResponse response) {
        return Optional.ofNullable(hm.getHandler(request))
                .map(handle(request, response))
                .orElseThrow(HandlerNotFoundException::new);
    }

    private Function<HandlerExecution, ModelAndView> handle(final HttpServletRequest request,
                                                            final HttpServletResponse response) {
        return handler -> {
            try {
                return handler.handle(request, response);
            } catch (final Throwable e) {
                logger.error("Exception ", e);
                throw new HandlingException(e);
            }
        };
    }
}


