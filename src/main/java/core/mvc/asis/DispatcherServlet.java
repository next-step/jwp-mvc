package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.handler.HandlerExecution;
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
        HandlerMappings.initialize();
        logger.info("DispatcherServlet initialization has completed!");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = getModelAndView(request, response);
        modelAndView.getView()
                .render(modelAndView.getModel(), request, response);
    }

    public ModelAndView getModelAndView(HttpServletRequest request, HttpServletResponse response) {
        HandlerExecution handlerExecution = HandlerMappings.findHandler(request);
        return handlerExecution.handle(request, response);
    }
}
