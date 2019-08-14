package core.mvc.asis;

import core.mvc.HandlerMapper;
import core.mvc.HandlerNotFoundException;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.NextGenerationHandlerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private List<HandlerMapper> handlerMappers;

    @Override
    public void init() {
        handlerMappers = List.of(new LegacyHandlerMapperAdapter(), new NextGenerationHandlerMapper());
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
