package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handlermapping.HandlerMapping;
import core.mvc.tobe.handlermapping.HandlerMappings;
import core.mvc.tobe.handlermapping.custom.AnnotationHandlerMapping;
import core.mvc.tobe.handlermapping.custom.UrlHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE_FOR_COMPONENT_SCAN = "core.mvc";

    @Override
    public void init() {
        HandlerMappings.addHandlerMapping(new UrlHandlerMapping());
        HandlerMappings.addHandlerMapping(new AnnotationHandlerMapping(BASE_PACKAGE_FOR_COMPONENT_SCAN));
        HandlerMappings.getHandlerMappings()
                .forEach(HandlerMapping::init);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        HandlerExecution handlerExecution = HandlerMappings.findHandler(req);

        ModelAndView modelAndView = handlerExecution.handle(req, resp);
        modelAndView.getView().render(modelAndView.getModel(), req, resp);
    }
}
