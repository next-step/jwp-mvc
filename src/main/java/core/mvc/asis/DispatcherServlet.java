package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.view.View;
import core.mvc.view.ViewResolvers;
import java.io.IOException;
import javassist.NotFoundException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings;
    private ViewResolvers viewResolvers;

    @Override
    public void init() {
        handlerMappings = new HandlerMappings();
        viewResolvers = new ViewResolvers();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            Object handler = handlerMappings.getHandler(req);
            ModelAndView mav = viewResolvers.resolve(handler, req, resp);
            View view = mav.getView();
            view.render(mav.getModel(), req, resp);
        } catch (NotFoundException ne) {
            logger.error("Not found : {}", ne.getMessage());
            resp.sendError(HttpStatus.NOT_FOUND.value(), ne.getMessage());
        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }
}
