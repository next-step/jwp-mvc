package core.mvc;

import core.mvc.asis.Controller;
import core.mvc.tobe.HandlerExecution;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MappingHandlerAdapter {

    private final MappingRegistry mappingRegistry;

    public MappingHandlerAdapter(MappingRegistry mappingRegistry) {
        this.mappingRegistry = mappingRegistry;
    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = mappingRegistry.getHandler(request);
        if (handler instanceof Controller) {
            move((Controller) handler, request, response);
            return;
        }

        render((HandlerExecution) handler, request, response);
    }

    private void move(Controller controller, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String viewName = controller.execute(req, resp);

        if (viewName.startsWith(RedirectView.DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(RedirectView.DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);
    }

    private void render(HandlerExecution handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ModelAndView modelAndView = handler.handle(req, resp);

        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), req, resp);
    }

}
