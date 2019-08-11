package core.mvc.asis;

import core.mvc.tobe.HandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static core.mvc.View.DEFAULT_REDIRECT_PREFIX;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private RequestMapping requestMapping;

    public RequestMappingHandlerAdapter(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
        requestMapping.initMapping();
    }

    @Override
    public boolean supports(HttpServletRequest req) {
        return requestMapping.containsController(req.getRequestURI());
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Controller controller = requestMapping.findController(req.getRequestURI());
        String viewName = controller.execute(req, resp);

        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        req.getRequestDispatcher(viewName).forward(req, resp);
    }
}
