package core.mvc.asis;

import core.mvc.HandlerExecutor;
import core.mvc.RequestMappingHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by hspark on 2019-08-16.
 */
public class LegacyRequestMappingHandler implements RequestMappingHandler {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private RequestMapping requestMapping = new RequestMapping();

    @Override
    public void initialize() {
        requestMapping.initMapping();
    }

    @Override
    public Optional<HandlerExecutor> getHandlerExecutor(HttpServletRequest httpServletRequest) {
        Controller controller = requestMapping.findController(httpServletRequest.getRequestURI());
        if (Objects.isNull(controller)) {
            return Optional.empty();
        }

        HandlerExecutor handlerExecutor = (request, response) -> {
            String viewName = controller.execute(request, response);
            move(viewName, request, response);
        };

        return Optional.of(handlerExecutor);
    }

    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);
    }
}
