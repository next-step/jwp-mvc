package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.handlermapping.HandlerMapping;
import core.mvc.tobe.handlermapping.HandlerMappingRegistry;
import core.mvc.tobe.handlermapping.HandlerMappings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet2 extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet2.class);

    private static Set<HandlerMapping> handlerMappings = new HashSet<>();

    @Override
    public void init() throws ServletException {
        HandlerMappingRegistry.register();
        handlerMappings = HandlerMappings.getHandlerMappings();
        handlerMappings.stream()
                .forEach(HandlerMapping::init);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HandlerMapping matched = findHandlerMapping(req);
        HandlerExecution handlerExecution = matched.findHandler(req);

        try {
            ModelAndView modelAndView = handlerExecution.handle(req, resp);
            modelAndView.getView()
                    .render(modelAndView.getModel(), req, resp);
        } catch (Exception e) {
            logger.error("Exception : {}", e);
            e.printStackTrace();
        }
    }

    private HandlerMapping findHandlerMapping(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.hasHandler(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found matched HandlerMapping"));
    }
}
