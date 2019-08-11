package core.mvc.asis;

import core.mvc.MappingHandlerAdapter;
import core.mvc.MappingRegistry;
import core.mvc.tobe.AnnotationHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private MappingHandlerAdapter mappingHandlerAdapter;

    @Override
    public void init() throws ServletException {
        MappingRegistry registry = new MappingRegistry(
                new AnnotationHandlerMapping(""), new RequestMapping()
        );

        mappingHandlerAdapter = new MappingHandlerAdapter(registry);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            mappingHandlerAdapter.handle(req, resp);
        } catch (Throwable e) {
            logger.error("Exception!!!", e);
            throw new ServletException(e.getMessage());
        }
    }

}
