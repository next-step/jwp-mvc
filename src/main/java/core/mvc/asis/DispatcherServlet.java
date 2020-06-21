package core.mvc.asis;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private RequestMapping requestMapping;

    @Override
    public void init() {
        requestMapping = new RequestMapping();
        requestMapping.initMapping();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        log.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        // TODO: 2020/06/21 이곳에서 as-is, to-be handler 가 공존해야 한다
        Controller controller = requestMapping.findController(requestUri);

        try {
            String viewName = controller.execute(req, resp);
            move(viewName, req, resp);
        } catch (Exception e) {
            log.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
        requestDispatcher.forward(req, resp);
    }
}
