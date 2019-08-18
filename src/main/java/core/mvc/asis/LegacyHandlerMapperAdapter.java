package core.mvc.asis;

import core.mvc.HandlerMapper;
import core.mvc.HandlerNotFoundException;
import core.mvc.HandlingException;
import core.mvc.ModelAndView;
import core.mvc.ViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class LegacyHandlerMapperAdapter implements HandlerMapper {

    private static final Logger logger = LoggerFactory.getLogger(LegacyHandlerMapperAdapter.class);

    private final RequestMapping requestMapping;

    LegacyHandlerMapperAdapter() {
        requestMapping = new RequestMapping();
        requestMapping.initMapping();
    }

    @Override
    public boolean isSupport(final HttpServletRequest request) {
        return Objects.nonNull(findController(request));
    }

    @Override
    public ModelAndView mapping(final HttpServletRequest request,
                                final HttpServletResponse response) {
        return Optional.ofNullable(findController(request))
                .map(handle(request, response))
                .map(ViewFactory::create)
                .map(ModelAndView::new)
                .orElseThrow(HandlerNotFoundException::new);
    }

    private Function<Controller, String> handle(final HttpServletRequest request,
                                                final HttpServletResponse response) {
        return controller -> {
            try {
                return controller.execute(request, response);
            } catch (final Throwable e) {
                logger.error("Exception ", e);
                throw new HandlingException(e);
            }
        };
    }

    private Controller findController(final HttpServletRequest request) {
        return requestMapping.findController(request.getRequestURI());
    }
}
