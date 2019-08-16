package core.mvc.asis;

import core.mvc.HandlerNotFoundException;
import core.mvc.RequestMappingHandler;
import core.mvc.tobe.AnnotationRequestMappingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<RequestMappingHandler> requestMappingHandlers;

    public DispatcherServlet() {
        requestMappingHandlers = Arrays.asList(
                new LegacyRequestMappingHandler(),
                new AnnotationRequestMappingHandler()
        );
    }


    @Override
    public void init() throws ServletException {
        requestMappingHandlers.forEach(RequestMappingHandler::initialize);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            requestMappingHandlers.stream()
                    .map(it -> it.getHandlerExecutor(req))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findAny().orElseThrow(() -> new HandlerNotFoundException(requestUri))
                    .execute(req, resp);

        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
}
