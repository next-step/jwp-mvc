package core.web.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewRenderer {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private static final ViewRenderer viewRenderer = new ViewRenderer();

    private ViewRenderer() {

    }

    public static ViewRenderer getInstance() {
        return viewRenderer;
    }

    public void render(String viewName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        move(viewName, req, resp);
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
