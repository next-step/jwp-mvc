package core.mvc.asis;

import core.mvc.HandlerMapper;
import core.mvc.HandlerNotFoundException;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.NextGenerationHandlerMapper;
import core.mvc.tobe.RequestParameterResolver;
import core.mvc.tobe.extractor.HttpServletRequestExtractor;
import core.mvc.tobe.extractor.HttpServletResponseExtractor;
import core.mvc.tobe.extractor.IntegerRequestParameterExtractor;
import core.mvc.tobe.extractor.LongRequestParameterExtractor;
import core.mvc.tobe.extractor.ObjectRequestParameterExtractor;
import core.mvc.tobe.extractor.PathVariableExtractor;
import core.mvc.tobe.extractor.RequestParameterExtractor;
import core.mvc.tobe.extractor.StringRequestParameterExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.function.Function;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final String DEFAULT_BASE_PACKAGE = "next.controller";

    private List<HandlerMapper> handlerMappers;

    @Override
    public void init() {
        final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        final List<RequestParameterExtractor> requestParameterExtractors = List.of(new HttpServletRequestExtractor(),
                new HttpServletResponseExtractor(),
                new PathVariableExtractor(),
                new IntegerRequestParameterExtractor(),
                new LongRequestParameterExtractor(),
                new StringRequestParameterExtractor(),
                new ObjectRequestParameterExtractor());

        final RequestParameterResolver requestParameterResolver = new RequestParameterResolver(parameterNameDiscoverer,
                requestParameterExtractors);

        final LegacyHandlerMapperAdapter legacyHandlerMapperAdapter = new LegacyHandlerMapperAdapter();
        final NextGenerationHandlerMapper nextGenerationHandlerMapper = new NextGenerationHandlerMapper(
                requestParameterResolver, DEFAULT_BASE_PACKAGE);

        handlerMappers = List.of(legacyHandlerMapperAdapter, nextGenerationHandlerMapper);
    }

    @Override
    protected void service(final HttpServletRequest request,
                           final HttpServletResponse response) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        final ModelAndView mav = handlerMappers.stream()
                .filter(handlerMapper -> handlerMapper.isSupport(request))
                .findFirst()
                .map(handle(request, response))
                .orElseThrow(HandlerNotFoundException::new);

        final View view = mav.getView();
        try {
            view.render(mav.getModel(), request, response);
        } catch (final Throwable e) {
            logger.error("Exception ", e);
            throw new ServletException(e);
        }
    }

    private Function<HandlerMapper, ModelAndView> handle(final HttpServletRequest request,
                                                         final HttpServletResponse response) {
        return handlerMapper -> handlerMapper.mapping(request, response);
    }
}
