package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerAdapter;
import core.mvc.tobe.HandlerAdapterImpl;
import core.mvc.tobe.HandlerMapping;
import core.mvc.view.View;
import exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private RequestMapping requestMapping;
    private HandlerAdapterImpl handlerAdapters;

    private AnnotationHandlerMapping handlerMapping;
    private final List<HandlerMapping> mappings = new ArrayList<>();

    @Override
    public void init() {
        requestMapping = new RequestMapping();
        handlerAdapters = new HandlerAdapterImpl();
        requestMapping.initMapping();
        handlerAdapters.initHandlerAdapters();

        handlerMapping = new AnnotationHandlerMapping();
        handlerMapping.initialize();
        mappings.add(requestMapping);
        mappings.add(handlerMapping);

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        Controller controller = getController(request);
        if (controller == null) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        HandlerAdapter adapter = handlerAdapters.getHandlerAdapter(controller);
        try {
            ModelAndView modelAndView = adapter.handle(request, response, controller);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Controller getController(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        return requestMapping.findController(requestURI);
    }
}
