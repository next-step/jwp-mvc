package core.mvc.asis;

import core.mvc.*;

import javax.servlet.http.HttpServletRequest;
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
            return createModelAndView(viewName);
        };

        return Optional.of(handlerExecutor);
    }

    private ModelAndView createModelAndView(String viewName) {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            return new ModelAndView(new RedirectView(viewName.substring(DEFAULT_REDIRECT_PREFIX.length())));
        }
        return new ModelAndView(new JspView(viewName.substring(DEFAULT_REDIRECT_PREFIX.length())));
    }
}
