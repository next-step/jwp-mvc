package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.RequestHandlerMappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);


    private RequestHandlerMappers mappers;

    @Override
    public void init() {
        mappers = new RequestHandlerMappers();
        mappers.addMapper(new RequestMapping());
        mappers.addMapper(new AnnotationHandlerMapping("next.controller"));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestURI = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestURI);

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
}
