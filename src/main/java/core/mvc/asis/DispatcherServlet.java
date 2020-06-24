package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.RequestHandlerMappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
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
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private RequestHandlerMappers mappers;

    @Override
    public void init() {
        mappers = new RequestHandlerMappers();
        mappers.addMapper(new RequestMapping());
        mappers.addMapper(new AnnotationHandlerMapping("next.controller"));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        ModelAndView modelAndView = mappers.mapperHandling(req, resp);
        render(modelAndView, req, resp);

    }

    private void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            mav.getView().render(mav.getModel(), request, response);
        } catch (Throwable e) {
            logger.error("Exception: {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);
    }
}
