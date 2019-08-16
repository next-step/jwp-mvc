package core.mvc.asis;

import core.mvc.Handler;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.ViewResolvers;
import core.mvc.tobe.HandlerAdapters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerAdapters handlerAdapters;
    private ViewResolvers viewResolvers;

    @Override
    public void init() throws ServletException {
        String basePackages = (String) getServletContext().getAttribute("basePackages");
        handlerAdapters = new HandlerAdapters(basePackages);
        viewResolvers = new ViewResolvers();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            Handler handler = handlerAdapters.getHandler(req, resp);

            if (handler == null) {
                resp.sendError(HttpStatus.NOT_FOUND.value());
                return;
            }

            ModelAndView modelAndView = handler.handle(req, resp);
            render(req, resp, modelAndView);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView mav) throws Exception {
        View view = viewResolvers.getView(mav.getViewName());
        view.render(mav.getModel(), req, resp);
    }

}
