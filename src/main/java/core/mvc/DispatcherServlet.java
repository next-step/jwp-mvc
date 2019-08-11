package core.mvc;

import core.mvc.tobe.Environment;
import core.mvc.tobe.HandlerAdapter;
import core.mvc.tobe.HandlerAdapterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerAdapter> handlerAdapters;

    @Override
    public void init() {
        HandlerAdapterFactory adapterFactory = new HandlerAdapterFactory(new Environment());
        this.handlerAdapters = adapterFactory.getHandlerAdapters();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        try {
            doService(req, resp);
        } catch (Throwable e) {
            logger.error("## Exception: {}", e.getMessage());
            throw new ServletException();
        }
    }

    private void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(req)) {
                handlerAdapter.handle(req, resp);
            }
        }
    }

    public void setHandlerAdapters(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }
}
