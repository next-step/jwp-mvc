package core.mvc;

import core.mvc.View;
import java.io.IOException;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {

    private static final String REDIRECT_PREFIX = "redirect:";

    private String view;

    public JspView(String viewName) {
        this.view = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        if(this.view.startsWith(REDIRECT_PREFIX)) {
            redirectRender(response);
            return;
        }

        viewRender(model, request, response);
    }

    private void redirectRender(HttpServletResponse response) throws IOException {
        response.sendRedirect(view.substring(REDIRECT_PREFIX.length()));
    }

    private void viewRender(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(this.view);
        model.forEach(request::setAttribute);
        requestDispatcher.forward(request, response);
    }
}
