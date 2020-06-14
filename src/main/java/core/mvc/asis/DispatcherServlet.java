package core.mvc.asis;

import core.exception.InternalServerErrorException;
import core.exception.NotFoundException;
import core.mvc.*;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "next.controller";

    private List<RequestHandlerMapping> requestHandlerMappings;
    private final List<ViewResolver> viewResolvers =
            Arrays.asList(new ControllerViewResolver(), new HandlerExecutionViewResolver());

    @Override
    public void init() throws ServletException {
        requestHandlerMappings = new ArrayList<>();

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        annotationHandlerMapping.initialize();

        RequestMapping requestMapping = new RequestMapping();
        requestMapping.initMapping();

        requestHandlerMappings.add(annotationHandlerMapping);
        requestHandlerMappings.add(requestMapping);
    }

    @Override
    protected void service(final HttpServletRequest request,
                           final HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = findHandler(request);

            handle(handler, request, response);
        } catch (NotFoundException e) {
            logger.error("{}", e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (InternalServerErrorException e) {
            logger.error("{}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handle(final Object handler,
                        final HttpServletRequest request,
                        final HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = resolve(handler, request, response);

        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }

    private ModelAndView resolve(final Object handler,
                                 final HttpServletRequest request,
                                 final HttpServletResponse response) {
        return viewResolvers.stream()
                    .map(resolver -> resolve(handler, request, response, resolver))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new InternalServerErrorException("Fail to find view resolver for " + handler));
    }

    private ModelAndView resolve(final Object handler,
                                 final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final ViewResolver resolver) {
        try {
            return resolver.handle(handler, request, response);
        } catch (Exception e) {
            logger.error("Fail to resolve view from handler's result");
            return null;
        }
    }

    private Object findHandler(final HttpServletRequest request) {
        return requestHandlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(request.getRequestURI()));
    }
}
