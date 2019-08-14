package core.mvc.asis;

import core.mvc.*;
import core.mvc.tobe.AnnotationHandlerAdapter;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private List<Handler> handlerAdapters = new ArrayList<>();

    private ViewResolvers viewResolvers = new ViewResolvers();

    @Override
    public void init() throws ServletException {
        handlerAdapters.add(new LegacyHandlerAdapter(createLegacyHandlerMapping()));
        handlerAdapters.add(new AnnotationHandlerAdapter(createAnnotationHandlerMapping()));
    }

    private AnnotationHandlerMapping createAnnotationHandlerMapping() {
        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("next.controller");
        ahm.initialize();
        return ahm;
    }

    private LegacyHandlerMapping createLegacyHandlerMapping() {
        LegacyHandlerMapping lhm = new LegacyHandlerMapping();
        lhm.initMapping();
        return lhm;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            ModelAndView modelAndView = handle(req, resp);
            render(req, resp, modelAndView);
        } catch (Exception e) {
            throw new ServletException("Handler not found", e);
        }
    }

    private ModelAndView handle(HttpServletRequest req, HttpServletResponse resp) {
        for (Handler handlerAdapter : handlerAdapters) {
            ModelAndView modelAndView = handlerAdapter.handle(req, resp);
            if (modelAndView != null) {
                return modelAndView;
            }
        }
        throw new IllegalArgumentException("Handler not found");
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView mav) throws Exception {
        View view = viewResolvers.getView(mav.getViewName());
        view.render(mav.getModel(), req, resp);
    }

}
