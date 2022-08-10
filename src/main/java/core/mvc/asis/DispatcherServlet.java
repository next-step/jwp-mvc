package core.mvc.asis;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.HandlerAdapter;
import core.mvc.HandlerAdapters;
import core.mvc.ModelAndView;
import core.mvc.HandlerMappings;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "next.controller";

    private HandlerMappings handlerMappings;
    private HandlerAdapters handlerAdapters;


    @Override
    public void init() {
        handlerMappings = new HandlerMappings(BASE_PACKAGE);
        handlerAdapters = new HandlerAdapters();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object mappingHandler = handlerMappings.getHandler(req);
        HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(mappingHandler);

        try {
            ModelAndView mv = handlerAdapter.handle(req, resp, mappingHandler);
            render(req, resp, mv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws Exception {
        modelAndView.getView().render(modelAndView.getModel(), req, resp);
    }
}
